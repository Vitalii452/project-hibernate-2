package com.budiak.service;

import com.budiak.dao.StoreDAO;
import com.budiak.model.Store;
import com.budiak.service.Exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class StoreService {

    private static final Logger LOGGER = LogManager.getLogger(CityService.class);
    private final StoreDAO storeDAO;

    public StoreService(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    public Store findStoreById(Session session, Byte id) {
        if (session == null || !session.isOpen()) {
            LOGGER.error("Session is closed or null");
            throw new ServiceException("Session is not open");
        }

        LOGGER.debug("Attempting to find store with id: {}", id);
        Store store = storeDAO.findById(session, id);
        if (store == null) {
            LOGGER.info("No store found with id: {}", id);
        } else {
            LOGGER.info("Store found with id: {}", id);
        }
        return store;
    }
}

