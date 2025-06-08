package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.domain.User;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class UserService
{
	private final UserDao userDao;

	public User create(String name, String email, int age)
	{
		var user = buildUser(name, email, age, new User());

		return userDao.create(user);
	}

	public User update(Long id, String name, String email, int age)
	{
		var oldUser = findById(id);
		var updatedUser = buildUser(name, email, age, oldUser);

		return userDao.update(updatedUser);
	}

	public User findById(Long id)
	{
		if (id == null)
		{
			throw new NullPointerException("Идентификатор должен быть заполнен.");
		}

		return userDao.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден", id));
	}

	public List<User> findAll()
	{
		return userDao.findAll();
	}

	public void delete(Long id)
	{
		if (id == null)
		{
			throw new NullPointerException("Идентификатор должен быть заполнен.");
		}

		var user = findById(id);

		userDao.delete(user);
	}

	private User buildUser(String name, String email, int age, User old)
	{
		old.setName(name);
		old.setEmail(email);
		old.setAge(age);

		return old;
	}
}
