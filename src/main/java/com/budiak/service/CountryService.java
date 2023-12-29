package com.budiak.service;

import com.budiak.dao.CountryDAO;
import com.budiak.model.Country;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CountryService {

    private static final Logger logger = LogManager.getLogger(CountryService.class);
    private final CountryDAO countryDAO;

    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public Country findOrCreateCountry(Session session, String countryName) {
        logger.debug("Attempting to find or create a country: {}", countryName);

        if (!session.getTransaction().isActive()) {
            logger.error("Session transaction not active for country: {}", countryName);
            throw new IllegalStateException("Session transaction required!");
        }

        Country countryObj = new Country(countryName);
        Country countryInBd = countryDAO.findMatchingCountry(session, countryObj);

        if (countryInBd == null) {
            logger.info("Creating a new country in the database: {}", countryName);
            countryDAO.save(session, countryObj);
            return countryObj;
        }

        logger.info("Country found in the database: {}", countryName);
        return countryInBd;
    }
}
