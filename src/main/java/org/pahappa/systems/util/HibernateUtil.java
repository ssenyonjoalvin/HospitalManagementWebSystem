package org.pahappa.systems.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            System.out.println("[DEBUG] Building Hibernate SessionFactory...");
            // Load the configuration and build the SessionFactory
            Configuration configuration = new Configuration().configure();
            System.out.println("[DEBUG] Hibernate configuration loaded successfully");
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            System.out.println("[DEBUG] Hibernate SessionFactory built successfully");
            return sessionFactory;
        } catch (Throwable ex) {
            System.err.println("[ERROR] Initial SessionFactory creation failed: " + ex.getMessage());
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}