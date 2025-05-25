package org.example.configuration;

import lombok.Getter;
import org.example.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateConfiguration
{
	@Getter
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory()
	{
		try
		{
			var standardRegistry = new StandardServiceRegistryBuilder()
					.configure("hibernate.cfg.xml")
					.build();

			var metadata = new MetadataSources(standardRegistry)
					.addAnnotatedClass(User.class)
					.getMetadataBuilder()
					.build();

			return metadata.getSessionFactoryBuilder()
					.build();
		}
		catch (Throwable ex)
		{
			System.err.println("Initial SessionFactory creation failed." + ex);

			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void shutdown()
	{
		getSessionFactory().close();
	}
}
