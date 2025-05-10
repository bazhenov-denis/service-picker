package com.example.backend.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    return new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static void shutdown() {
    getSessionFactory().close();
  }
}
