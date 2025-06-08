package org.example;

import org.example.configuration.HibernateConfiguration;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			ConsoleService.menu();
		}
		finally
		{
			HibernateConfiguration.shutdown();
		}
	}
}