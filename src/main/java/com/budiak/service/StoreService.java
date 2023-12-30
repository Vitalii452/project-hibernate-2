package com.budiak.service;

import com.budiak.dao.StoreDAO;
import com.budiak.model.Store;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class StoreService {

    private static final Logger logger = LogManager.getLogger(StoreService.class);
    private final StoreDAO storeDAO;
    private final SessionFactory sessionFactory;

    public StoreService(StoreDAO storeDAO, SessionFactory sessionFactory) {
        this.storeDAO = storeDAO;
        this.sessionFactory = sessionFactory;
    }

    public Store findStoreById(Byte id) {
        logger.debug("Attempting to find store with id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            Store store = storeDAO.findById(session, id);
            if (store == null) {
                logger.info("No store found with id: {}", id);
            } else {
                logger.info("Store found with id: {}", id);
            }
            return store;
        } catch (Exception e) {
            logger.error("Error finding store with id: {}", id, e);
            throw e;
        }
    }
}
