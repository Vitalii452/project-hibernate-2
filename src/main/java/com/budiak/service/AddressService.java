package com.budiak.service;

import com.budiak.dao.AddressDAO;
import com.budiak.model.Address;
import com.budiak.model.City;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class AddressService {

    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private final AddressDAO addressDAO;
    private final CityService cityService;

    public AddressService(AddressDAO addressDAO, CityService cityService) {
        this.addressDAO = addressDAO;
        this.cityService = cityService;
    }

    /**
     * Finds or creates an address in the database.
     * If the given address already exists in the database, it returns the existing address.
     * If the given address does not exist in the database, it creates a new address and returns it.
     *
     * @param session The Hibernate session to use for the operations.
     * @param newAddress The new address to find or create.
     * @return The existing or newly created address.
     */
    public Address findOrCreateAddress(Session session, Address newAddress) {
        HibernateUtil.validateSessionAndTransaction(session);
        LOGGER.debug("Attempting to find or create an address for: {}", newAddress.getAddress());

        updateCityOfAddress(session, newAddress);

        Address existingAddress = findExistingAddress(session, newAddress);
        if (existingAddress == null) {
            return saveAddress(session, newAddress);
        } else {
            LOGGER.info("Address found in the database: {}", existingAddress.getAddress());
            return existingAddress;
        }
    }

    private void updateCityOfAddress(Session session, Address newAddress) {
        City cityInDb = cityService.findOrCreateCity(session, newAddress.getCity());
        newAddress.setCity(cityInDb);
    }

    private Address findExistingAddress(Session session, Address address) {
        try {
            return addressDAO.findMatchingAddress(session, address);
        } catch (Exception e) {
            LOGGER.error("Error finding address", e);
            throw new ServiceException("Error finding address", e);
        }
    }

    private Address saveAddress(Session session, Address address) {
        try {
            LOGGER.info("Creating a new address in the database");
            addressDAO.save(session, address);
            return address;
        } catch (Exception e) {
            LOGGER.error("Error saving address: {}", e.getMessage(), e);
            throw new ServiceException("Error saving address", e);
        }
    }
}

