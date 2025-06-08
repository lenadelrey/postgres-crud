package utils;

import org.example.domain.User;
import org.hibernate.SessionFactory;

public class TestDataCreator
{
	public static void buildTestData(SessionFactory sessionFactory)
	{
		try (var session = sessionFactory.openSession())
		{
			var transaction = session.beginTransaction();

			var user = new User();

			user.setName("name");
			user.setEmail("email");
			user.setAge(25);

			session.persist(user);

			var user1 = new User();

			user1.setName("name");
			user1.setEmail("em4ail");
			user1.setAge(25);

			session.persist(user1);

			var user2 = new User();

			user2.setName("name");
			user2.setEmail("em2ail");
			user2.setAge(25);

			session.persist(user2);
			transaction.commit();
		}
	}

	public static User buildTestUser(Long id, String name, String email, int age)
	{
		var user = new User();

		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		user.setAge(age);

		return user;
	}

	public static void clearTestData(SessionFactory sessionFactory)
	{
		try (var session = sessionFactory.openSession())
		{
			var transaction = session.beginTransaction();
			session.createMutationQuery("delete from User")
					.executeUpdate();

			transaction.commit();
		}
	}
}
