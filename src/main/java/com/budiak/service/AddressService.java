package com.budiak.service;

import com.budiak.dao.AddressDAO;
import com.budiak.model.Address;
import com.budiak.model.City;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class AddressService {

    private static final Logger logger = LogManager.getLogger(AddressService.class);
    private final AddressDAO addressDAO;
    private final CityService cityService;

    public AddressService(AddressDAO addressDAO, CityService cityService) {
        this.addressDAO = addressDAO;
        this.cityService = cityService;
    }

    public Address findOrCreateAddress(Session session, String address, String address2, String district, City city, String postalCode, String phone) {
        logger.debug("Attempting to find or create an address for: {}", city.getCity());

        if (!session.getTransaction().isActive()) {
            logger.error("Session transaction not active!");
            throw new IllegalStateException("Session transaction required!");
        }

        City cityInDb = cityService.findOrCreateCity(session, city.getCity(), city.getCountry());
        Address addressObj = new Address(address, address2, district, cityInDb, postalCode, phone);
        Address addressInDb = addressDAO.findMatchingAddress(session, addressObj);

        if (addressInDb == null) {
            logger.info("Creating a new address in the database");
            addressDAO.save(session, addressObj);
            return addressObj;
        }

        logger.info("Address found in the database: {}", addressInDb.getAddress());
        return addressInDb;
    }
}

