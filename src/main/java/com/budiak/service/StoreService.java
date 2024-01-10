package com.budiak.service;

import com.budiak.dao.StoreDAO;
import com.budiak.model.Store;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class StoreService {

    private static final Logger LOGGER = LogManager.getLogger(StoreService.class);
    private final StoreDAO storeDAO;

    public StoreService(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    /**
     * Finds a store by ID.
     *
     * @param session The Hibernate session.
     * @param id The ID of the store to find.
     * @return The store matching the specified ID, or null if not found.
     * @throws ServiceException If an error occurs while finding the store.
     */
    public Store findStoreById(Session session, Byte id) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find store with id: {}", id);

        Store store = null;
        try {
            store = storeDAO.findById(session, id);

        } catch (Exception e) {
            LOGGER.error("Error finding store", e);
            throw new ServiceException("Error finding store", e);
        }

        if (store == null) {
            LOGGER.info("No store found with id: {}", id);
        } else {
            LOGGER.info("Store found with id: {}", id);
        }
        return store;
    }
}

