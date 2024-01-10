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

    public Address findOrCreateAddress(Session session, Address newAddress) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to find or create an address for: {}", newAddress.getAddress());

        City cityInDb = cityService.findOrCreateCity(session, newAddress.getCity());
        newAddress.setCity(cityInDb);

        Address existingAddress = null;
        try {
            existingAddress = addressDAO.findMatchingAddress(session, newAddress);

        } catch (Exception e) {
            LOGGER.error("Error finding address", e);
            throw new ServiceException("Error finding address", e);
        }

        if (existingAddress == null) {
            try {
                LOGGER.info("Creating a new address in the database");
                addressDAO.save(session, newAddress);
                return newAddress;

            } catch (Exception e) {
                LOGGER.error("Error saving address: {}", e.getMessage(), e);
                throw new ServiceException("Error saving address", e);
            }
        }

        LOGGER.info("Address found in the database: {}", existingAddress.getAddress());
        return existingAddress;
    }
}

