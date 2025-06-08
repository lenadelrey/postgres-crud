package org.example.dao;

import org.example.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao
{
	User create(User user);

	User update(User user);

	Optional<User> findById(Long id);

	List<User> findAll();

	void delete(User user);
}
