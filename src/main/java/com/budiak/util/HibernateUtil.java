package com.budiak.util;

import com.budiak.service.Exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final Logger LOGGER = LogManager.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void validateSessionAndTransaction(Session session) {
        if (session == null || !session.isOpen() || !session.getTransaction().isActive()) {
            LOGGER.error("Invalid session state");
            throw new ServiceException("Session is not open or transaction is not active");
        }
    }

    public static void validateSession(Session session) {
        if (session == null || !session.isOpen()) {
            LOGGER.error("Invalid session state");
            throw new ServiceException("Session is not open");
        }
    }
}
