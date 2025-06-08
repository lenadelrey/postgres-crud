package service;

import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.service.UserService;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.TestDataCreator;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
	@Mock
	private UserDao userDao;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setup()
	{
		userDao = mock(UserDao.class);
		userService = new UserService(userDao);
	}

	@Test
	public void create_whenOk()
	{
		var name = "name";
		var email = "test@mail.ru";
		var age = 25;

		var expected = TestDataCreator.buildTestUser(1L, name, email, age);

		when(userDao.create(any(User.class)))
				.thenReturn(expected);

		var result = userService.create(name, email, age);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(email, result.getEmail());

		verify(userDao, times(1)).create(any(User.class));
	}

	@Test
	public void update_whenFailed()
	{
		var id = 333L;

		when(userDao.findById(id)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class,
				() -> userService.update(id, "name", "email", 30));

		verify(userDao, never()).update(any());
	}

	@Test
	public void delete_whenOk()
	{
		var id = 1L;
		var user = TestDataCreator.buildTestUser(id, "name", "delete@mail.ru", 25);

		when(userDao.findById(id))
				.thenReturn(Optional.of(user));

		doNothing().when(userDao)
				.delete(user);

		assertDoesNotThrow(() -> userService.delete(id));

		verify(userDao, times(1)).findById(id);
		verify(userDao, times(1)).delete(user);
	}

	@Test
	public void deleteWhenFailed()
	{
		var id = 333L;

		when(userDao.findById(id))
				.thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> userService.delete(id));

		verify(userDao, never()).delete(any());
	}

	@Test
	public void findById_whenNotExists()
	{
		var id = 333L;

		when(userDao.findById(id))
				.thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class,
				() -> userService.findById(id));

		verify(userDao, times(1)).findById(id);
	}

	@Test
	public void findById_whenOk()
	{
		var id = 1L;
		var expected = TestDataCreator.buildTestUser(id, "name1", "email1", 25);

		when(userDao.findById(id)).thenReturn(Optional.of(expected));

		var result = userService.findById(id);

		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals("name1", result.getName());

		verify(userDao, times(1)).findById(id);
	}
}
