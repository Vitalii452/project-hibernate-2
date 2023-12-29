package com.budiak.service;

import com.budiak.dao.AbstractDAO;
import com.budiak.dao.StoreDAO;
import com.budiak.model.Store;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class StoreService {

    private final SessionFactory sessionFactory;
    private final StoreDAO storeDAO;

    public StoreService(SessionFactory sessionFactory, StoreDAO storeDAO) {
        this.sessionFactory = sessionFactory;
        this.storeDAO = storeDAO;
    }

    public Store findStoreById(Byte id) {
        try (Session session = sessionFactory.openSession()) {
            return storeDAO.findById(session, id);
        }
    }
}
