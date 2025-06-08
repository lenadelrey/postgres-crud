package dao;

import org.example.configuration.HibernateConfiguration;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.domain.User;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.TestDataCreator;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest
{
	@Container
	private static final PostgreSQLContainer<?> postgres =
			new PostgreSQLContainer<>("postgres:15-alpine")
					.withDatabaseName("my_app_db")
					.withUsername("my_app_user")
					.withPassword("postgres");

	private static UserDao userDao;
	private static SessionFactory sessionFactory;

	@BeforeAll
	public static void setup()
	{
		var configuration = new Configuration();

		configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
		configuration.setProperty("hibernate.connection.username", postgres.getUsername());
		configuration.setProperty("hibernate.connection.password", postgres.getPassword());
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		configuration.addAnnotatedClass(User.class);

		sessionFactory = configuration.buildSessionFactory();
		userDao = new UserDaoImpl();

		HibernateConfiguration.setSessionFactory(sessionFactory);
		TestDataCreator.buildTestData(sessionFactory);
	}

	@AfterAll
	public static void close()
	{
		TestDataCreator.clearTestData(sessionFactory);

		if (sessionFactory != null)
		{
			sessionFactory.close();
		}
	}

	@Test
	void create_whenOk()
	{
		var newUser = new User();

		newUser.setName("Test User");
		newUser.setEmail("test@mail.ru");
		newUser.setAge(24);

		var createdUser = userDao.create(newUser);

		assertNotNull(createdUser);
		assertNotNull(createdUser.getId());
	}

	@Test
	void update_whenOk()
	{
		var user = new User();

		user.setId(4L);
		user.setEmail("test@mail.ru");

		var updatedUser = userDao.update(user);

		assertNotNull(updatedUser);
		assertEquals(4L, updatedUser.getId());
		assertEquals("test@mail.ru", updatedUser.getEmail());
	}

	@Test
	void update_whenFailed()
	{
		var user = new User();

		user.setName("name");
		user.setEmail("test1@mail.ru");
		user.setAge(25);
		userDao.create(user);

		user.setEmail("email");

		assertThrows(RuntimeException.class, () -> userDao.update(user));
	}

	@Test
	void delete_whenOk()
	{
		var user2 = new User();

		user2.setName("name");
		user2.setEmail("em2ail");
		user2.setAge(25);

		userDao.delete(user2);

		assertThrows(ObjectNotFoundException.class, () -> userDao.findById(user2.getId()));
	}

	@Test
	void findById_whenObjectNotFound()
	{
		var id = 225L;

		assertThrows(ObjectNotFoundException.class, () -> userDao.findById(id));
	}
}
