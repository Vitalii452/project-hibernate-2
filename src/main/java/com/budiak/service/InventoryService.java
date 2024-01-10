package com.budiak.service;

import com.budiak.dao.InventoryDAO;
import com.budiak.model.Film;
import com.budiak.model.Inventory;
import com.budiak.model.Store;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class InventoryService {

    private static final Logger LOGGER = LogManager.getLogger(InventoryService.class);
    private final InventoryDAO inventoryDAO;
    private final StoreService storeService;

    public InventoryService(InventoryDAO inventoryDAO, StoreService storeService) {
        this.inventoryDAO = inventoryDAO;
        this.storeService = storeService;
    }

    /**
     * Finds the inventory by film ID and store ID.
     *
     * @param session The Hibernate session.
     * @param filmId  The ID of the film.
     * @param storeId The ID of the store.
     * @return The inventory matching the film ID and store ID, or null if not found.
     * @throws ServiceException If an error occurs while finding the inventory.
     */
    public Inventory findInventoryByFilmAndStore(Session session, short filmId, byte storeId) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find inventory with filmId {} and storeId {}", filmId, storeId);

        try {
            Inventory inventory = inventoryDAO.findInventoryByDetails(session, filmId, storeId);

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

    /**
     * Adds a film to the inventory.
     *
     * @param session The Hibernate session.
     * @param film    The film to be added to the inventory.
     * @param storeId The ID of the store where the film will be added.
     * @throws ServiceException If an error occurs while adding the film to the inventory.
     */
    public void addFilmToInventory(Session session, Film film, Byte storeId) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to create inventory with filmId {} and storeId {}", film.getFilmId(), storeId);

        try {
            LOGGER.info("Creating a new inventory in the database");
            Store store = storeService.findStoreById(session, storeId);

            Inventory newInventory = new Inventory(film, store);
            inventoryDAO.save(session, newInventory);

        } catch (Exception e) {
            LOGGER.error("Error creating new inventory: {}", e.getMessage(), e);
            throw new ServiceException("Error creating new inventory", e);
        }
    }
}
