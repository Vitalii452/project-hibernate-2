package com.budiak.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

public class TransactionUtils {

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
