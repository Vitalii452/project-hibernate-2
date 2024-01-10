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

    public Country findOrCreateCountry(Session session, Country country) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to find or create a country: {}", country.getCountryName());

        Country newCountry = new Country(country.getCountryName());
        Country existingCountry = null;
        try {
            existingCountry = countryDAO.findMatchingCountry(session, newCountry);

        } catch (Exception e) {
            LOGGER.error("Error finding country", e);
            throw new ServiceException("Error finding country", e);
        }

        if (existingCountry == null) {
            try {
                LOGGER.info("Creating a new country in the database: {}", country.getCountryName());
                countryDAO.save(session, newCountry);

            } catch (Exception e) {
                LOGGER.error("Error saving country: {}", e.getMessage(), e);
                throw new ServiceException("Error saving country", e);
            }
            return newCountry;
        }

        LOGGER.info("Country found in the database: {}", country.getCountryName());
        return existingCountry;
    }
}
