package com.budiak.service;

import com.budiak.dao.CountryDAO;
import com.budiak.model.Country;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CountryService {

    private static final Logger LOGGER = LogManager.getLogger(CountryService.class);
    private final CountryDAO countryDAO;

    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public Country findOrCreateCountry(Session session, String countryName) {
        if (session == null || countryName == null || countryName.isEmpty()) {
            throw new IllegalArgumentException("Session and countryName must not be null or empty");
        }

        LOGGER.debug("Attempting to find or create a country: {}", countryName);

        if (!session.getTransaction().isActive()) {
            LOGGER.error("Session transaction not active for country: {}", countryName);
            throw new IllegalStateException("Session transaction required!");
        }

        Country newCountry = new Country(countryName);
        Country existingCountry = countryDAO.findMatchingCountry(session, newCountry);

        if (existingCountry == null) {
            LOGGER.info("Creating a new country in the database: {}", countryName);
            countryDAO.save(session, newCountry);
            return newCountry;
        }

        LOGGER.info("Country found in the database: {}", countryName);
        return existingCountry;
    }
}
