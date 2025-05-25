package org.example.dao;

import org.example.domain.User;

import java.util.List;

public interface UserDao
{
	Long create(String name, String email, int age);

	void update(User user);

	User findById(Long id);

	List<User> findAll();

	void delete(Long id);
}
