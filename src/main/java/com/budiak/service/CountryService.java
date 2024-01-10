package com.budiak.service;

import com.budiak.dao.CountryDAO;
import com.budiak.model.Country;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CountryService {

    private static final Logger LOGGER = LogManager.getLogger(CountryService.class);
    private final CountryDAO countryDAO;

    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    /**
     * Finds or creates a country in the database.
     * If the given country already exists in the database, it returns the existing country.
     * If the given country does not exist in the database, it creates a new country and returns it.
     *
     * @param session    The Hibernate session to use for the operations.
     * @param newCountry The new country to find or create.
     * @return The existing or newly created country.
     */
    public Country findOrCreateCountry(Session session, Country newCountry) {
        HibernateUtil.validateSessionAndTransaction(session);
        LOGGER.debug("Attempting to find or create a country: {}", newCountry.getCountryName());

        Country existingCountry = findExistingCountry(session, newCountry);
        if (existingCountry == null) {
            return saveCountry(session, newCountry);
        } else {
            LOGGER.debug("Country already exists in the database: {}", existingCountry.getCountryName());
            return existingCountry;
        }
    }

    private Country findExistingCountry(Session session, Country country) {
        try {
            return countryDAO.findMatchingCountry(session, country);
        } catch (Exception e) {
            LOGGER.error("Error finding country", e);
            throw new ServiceException("Error finding country", e);
        }
    }

    private Country saveCountry(Session session, Country newCountry) {
        try {
            LOGGER.info("Creating a new country in the database");
            countryDAO.save(session, newCountry);
            return newCountry;
        } catch (Exception e) {
            LOGGER.error("Error saving country: {}", e.getMessage(), e);
            throw new ServiceException("Error saving country", e);
        }
    }
}
