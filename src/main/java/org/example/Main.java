package org.example;

import org.example.configuration.HibernateConfiguration;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.domain.User;

import java.util.Scanner;

public class Main
{
	private static final UserDao userDao = new UserDaoImpl();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args)
	{
		try
		{
			menu();
		}
		finally
		{
			HibernateConfiguration.shutdown();
		}
	}

	private static void menu()
	{
		var running = true;

		while (running)
		{
			System.out.println("\nВыберите действие");
			System.out.println("1. Создание юзера");
			System.out.println("2. Получение юзера по id");
			System.out.println("3. Получение всех юзеров");
			System.out.println("4. Обновление данных юзера");
			System.out.println("5. Удаление юзера по id");
			System.out.println("6. Выход");

			try
			{
				var number = Integer.parseInt(scanner.nextLine());

				switch (number)
				{
					case 1 -> createUser();
					case 2 -> getById();
					case 3 -> getAll();
					case 4 -> update();
					case 5 -> deleteById();
					case 6 -> running = false;
					default -> System.out.println("Выберите действие.");
				}
			}
			catch (Exception e)
			{
				System.out.println("При взаимодействии с системой произошла ошибка: " + e.getMessage());
			}
		}
	}

	private static void createUser()
	{
		System.out.print("Имя: ");
		var name = scanner.nextLine();

		System.out.print("Email: ");
		var email = scanner.nextLine();

		System.out.print("Возраст: ");
		var age = Integer.parseInt(scanner.nextLine());

		var id = userDao.create(name, email, age);
		System.out.println("Пользователь создан. id пользователя: " + id);
	}

	private static void getById()
	{
		System.out.print("Введите id пользователя: ");
		var id = Long.parseLong(scanner.nextLine());

		var user = userDao.findById(id);

		if (user != null)
		{
			printUser(user);
		}
		else
		{
			System.out.printf("Пользователь с id = %s не найден.", id);
		}
	}

	private static void getAll()
	{
		var users = userDao.findAll();

		if (users.isEmpty())
		{
			System.out.println("Список пользователей пуст.");
		}
		else
		{
			users.forEach(Main::printUser);
		}
	}

	private static void update()
	{
		System.out.print("Введите id пользователя: ");

		var id = Long.parseLong(scanner.nextLine());
		var user = userDao.findById(id);

		if (user == null)
		{
			System.out.printf("Пользователь с id = %s не найден.", id);

			return;
		}

		System.out.print("Имя: ");
		var name = scanner.nextLine();

		if (!name.isEmpty())
		{
			user.setName(name);
		}

		System.out.print("Email: ");
		var email = scanner.nextLine();

		if (!email.isEmpty())
		{
			user.setEmail(email);
		}

		System.out.print("Возраст: ");
		var ageInput = scanner.nextLine();

		if (!ageInput.isEmpty())
		{
			user.setAge(Integer.parseInt(ageInput));
		}

		userDao.update(user);
		System.out.println("Данные обновлены.");
	}

	private static void deleteById()
	{
		System.out.print("Введите id пользователя: ");
		var id = Long.parseLong(scanner.nextLine());

		var user = userDao.findById(id);

		if (user != null)
		{
			userDao.delete(id);
			System.out.println("Пользователь удален.");
		}
		else
		{
			System.out.printf("Пользователь с id = %s не найден.", id);
		}
	}

	private static void printUser(User user)
	{
		System.out.printf("ID: %d | Имя: %s | Email: %s | Возраст: %d | Дата создания: %s%n",
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getAge(),
				user.getCreatedAt());
	}
}