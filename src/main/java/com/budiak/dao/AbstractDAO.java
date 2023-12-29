package com.budiak.dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(Session session, ID id) {
        return session.get(entityClass, id);
    }

    public void save(Session session, T entity) {
        session.save(entity);
    }

    public void update(Session session, T entity) {
        session.update(entity);
    }

    public void delete(Session session, T entity) {
        session.delete(entity);
    }

    public List<T> findAll(Session session) {
        return session.createQuery("from " + entityClass.getName(), entityClass).list();
    }
}
