package com.budiak.service;

import com.budiak.dao.InventoryDAO;
import com.budiak.model.Inventory;
import com.budiak.service.Exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class InventoryService {

    private static final Logger LOGGER = LogManager.getLogger(InventoryService.class);
    private final InventoryDAO inventoryDAO;

    public InventoryService(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public Inventory findAvailableInventoryByFilmAndStoreId(Session session, short filmId, byte storeId) {
        LOGGER.debug("Attempting to find inventory with filmId {} and storeId {}", filmId, storeId);

        if (session == null || !session.isOpen()) {
            LOGGER.error("Session is closed or null for filmId {} and storeId {}", filmId, storeId);
            throw new ServiceException("Session is not open");
        }

        try {
            Inventory inventory = inventoryDAO.findInventoryByFilmAndStoreId(session, filmId, storeId);
            if (inventory == null) {
                LOGGER.info("No inventory found with filmId {} and storeId {}", filmId, storeId);
                return null;
            } else {
                LOGGER.info("Inventory found with id: {}", inventory.getInventoryId());
                return inventory;
            }
        } catch (Exception e) {
            LOGGER.error("Error finding inventory for filmId {} and storeId {}: {}", filmId, storeId, e.getMessage(), e);
            throw new ServiceException("Error finding inventory", e);
        }
    }
}
