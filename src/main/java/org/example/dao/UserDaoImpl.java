package org.example.dao;

import org.example.configuration.HibernateConfiguration;
import org.example.domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoImpl implements UserDao
{
	@Override
	public Long create(String name, String email, int age)
	{
		Transaction transaction = null;

		try (var session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			var newUser = buildUser(name, email, age, new User());
			var user = session.merge(newUser);

			transaction.commit();

			return user.getId();
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}

			throw new RuntimeException("При создании пользователя произошла ошибка:", e);
		}
	}

	@Override
	public void update(User newUser)
	{
		Transaction transaction = null;

		try (Session session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			var oldUser = findById(newUser.getId());
			var updatedUser = buildUser(
					newUser.getName(),
					newUser.getEmail(),
					newUser.getAge(),
					oldUser
			);

			session.merge(updatedUser);
			transaction.commit();
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}

			throw new RuntimeException("При обновлении пользователя с id = %s произошла ошибка:".formatted(newUser.getId()), e);
		}
	}

	@Override
	public User findById(Long id)
	{
		try (var session = HibernateConfiguration.getSessionFactory().openSession())
		{
			return session.get(User.class, id);
		}
		catch (Exception e)
		{
			throw new RuntimeException("При получении пользователя с id = %s произошла ошибка:".formatted(id), e);
		}
	}

	@Override
	public List<User> findAll()
	{
		try (var session = HibernateConfiguration.getSessionFactory().openSession())
		{
			var query = session.createQuery("from User", User.class);

			return query.list();
		}
		catch (Exception e)
		{
			throw new RuntimeException("При получении списка пользователей произошла ошибка:", e);
		}
	}

	@Override
	public void delete(Long id)
	{
		Transaction transaction = null;

		try (Session session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			var user = findById(id);

			session.remove(user);
			transaction.commit();
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}

			throw new RuntimeException("При удалении пользователя с id = %s произошла ошибка:".formatted(id), e);
		}
	}

	private User buildUser(String name, String email, int age, User old)
	{
		old.setName(name);
		old.setEmail(email);
		old.setAge(age);

		return old;
	}
}
