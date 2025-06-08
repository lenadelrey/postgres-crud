package org.example.dao;

import org.example.configuration.HibernateConfiguration;
import org.example.domain.User;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao
{
	@Override
	public User create(User newUser)
	{
		Transaction transaction = null;

		try (var session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			session.persist(newUser);
			transaction.commit();

			return newUser;
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
	public User update(User newUser)
	{
		Transaction transaction = null;

		try (Session session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			var user = session.merge(newUser);
			transaction.commit();

			return user;
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
	public Optional<User> findById(Long id)
	{
		try (var session = HibernateConfiguration.getSessionFactory().openSession())
		{
			return Optional.of(session.get(User.class, id));
		}
		catch (Exception e)
		{
			throw new ObjectNotFoundException("При получении пользователя с id = %s произошла ошибка:".formatted(id), e);
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
	public void delete(User user)
	{
		Transaction transaction = null;

		try (Session session = HibernateConfiguration.getSessionFactory().openSession())
		{
			transaction = session.beginTransaction();

			session.remove(user);
			transaction.commit();
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}

			throw new RuntimeException("При удалении пользователя с id = %s произошла ошибка:".formatted(user.getId()), e);
		}
	}
}
