package com.budiak.service;

import com.budiak.dao.AddressDAO;
import com.budiak.model.Address;
import com.budiak.model.City;
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

    public Address findOrCreateAddress(Session session, String address, String address2, String district, City city, String postalCode, String phone) {
        if (session == null || city == null) {
            throw new IllegalArgumentException("Session and city must not be null");
        }

        LOGGER.debug("Attempting to find or create an address for: {}", city.getCityName());

        if (!session.getTransaction().isActive()) {
            LOGGER.error("Session transaction not active for city: {}", city.getCityName());
            throw new IllegalStateException("Session transaction required!");
        }

        City cityInDb = cityService.findOrCreateCity(session, city.getCityName(), city.getCountry());
        Address newAddress = new Address(address, address2, district, cityInDb, postalCode, phone);
        Address existingAddress = addressDAO.findMatchingAddress(session, newAddress);

        if (existingAddress == null) {
            LOGGER.info("Creating a new address in the database");
            addressDAO.save(session, newAddress);
            return newAddress;
        }

        LOGGER.info("Address found in the database: {}", existingAddress.getAddress());
        return existingAddress;
    }
}

