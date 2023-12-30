package com.budiak.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

/**
 * Utility class for handling transactions with Hibernate.
 */
public class TransactionUtils {

    /**
     * Executes an action within a transactional context.
     *
     * @param session the Hibernate session
     * @param action  the action to be executed
     * @throws RuntimeException if the transaction fails
     */
    public static void executeInTransaction(Session session, Runnable action) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            action.run();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a function within a transactional context and returns a result.
     *
     * @param <T>     the type of the result
     * @param session the Hibernate session
     * @param action  the function to be executed
     * @return the result of the function
     * @throws RuntimeException if the transaction fails
     */
    public static <T> T executeInTransaction(Session session, Function<Session, T> action) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }
}
