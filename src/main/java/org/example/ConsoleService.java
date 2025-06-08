package org.example;

import org.example.dao.UserDaoImpl;
import org.example.domain.User;
import org.example.service.UserService;

import java.util.Scanner;

public class ConsoleService
{
	private static final UserService userService = new UserService(new UserDaoImpl());
	private static final Scanner scanner = new Scanner(System.in);

	public static void menu()
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

		userService.create(name, email, age);
		System.out.println("Пользователь создан.");
	}

	private static void getById()
	{
		System.out.print("Введите id пользователя: ");
		var id = Long.parseLong(scanner.nextLine());

		var user = userService.findById(id);

		if (user != null)
		{
			printUser(user);
			return;
		}

		System.out.printf("Пользователь с id = %s не найден.", id);
	}

	private static void getAll()
	{
		var users = userService.findAll();

		if (users.isEmpty())
		{
			System.out.println("Список пользователей пуст.");
			return;
		}

		users.forEach(ConsoleService::printUser);
	}

	private static void update()
	{
		System.out.print("Введите id пользователя: ");
		var id = Long.parseLong(scanner.nextLine());

		System.out.print("Имя: ");
		var name = scanner.nextLine();

		System.out.print("Email: ");
		var email = scanner.nextLine();

		System.out.print("Возраст: ");
		var age = scanner.nextLine();

		userService.update(id, name, email, Integer.parseInt(age));
		System.out.println("Данные обновлены.");
	}

	private static void deleteById()
	{
		System.out.print("Введите id пользователя: ");
		var id = Long.parseLong(scanner.nextLine());

		userService.delete(id);
		System.out.println("Пользователь удален.");
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
