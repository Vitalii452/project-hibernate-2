package com.budiak.service;

import com.budiak.dao.CityDAO;
import com.budiak.model.City;
import com.budiak.model.Country;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CityService {

    private static final Logger LOGGER = LogManager.getLogger(CityService.class);
    private final CityDAO cityDAO;
    private final CountryService countryService;

    public CityService(CityDAO cityDAO, CountryService countryService) {
        this.cityDAO = cityDAO;
        this.countryService = countryService;
    }

    /**
     * Finds or creates a city in the database.
     * If the given city already exists in the database, it returns the existing city.
     * If the given city does not exist in the database, it creates a new city and returns it.
     *
     * @param session The Hibernate session to use for the operations.
     * @param newCity The new city to find or create.
     * @return The existing or newly created city.
     */
    public City findOrCreateCity(Session session, City newCity) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to find or create a city: {}", newCity.getCityName());

        updateCountryOfCity(session, newCity);

        City existingCity = findExistingCity(session, newCity);
        if (existingCity == null) {
            return saveCity(session, newCity);
        } else {
            LOGGER.info("City found in the database: {}", existingCity.getCityName());
            return existingCity;
        }
    }

    private void updateCountryOfCity(Session session, City newCity) {
        Country countryInDb = countryService.findOrCreateCountry(session, newCity.getCountry());
        newCity.setCountry(countryInDb);
    }

    private City findExistingCity(Session session, City city) {
        try {
            return cityDAO.findMatchingCity(session, city);
        } catch (Exception e) {
            LOGGER.error("Error finding city", e);
            throw new ServiceException("Error finding city", e);
        }
    }

    private City saveCity(Session session, City city) {
        try {
            LOGGER.info("Creating a new city in the database");
            cityDAO.save(session, city);
            return city;
        } catch (Exception e) {
            LOGGER.error("Error saving city: {}", e.getMessage(), e);
            throw new ServiceException("Error saving city", e);
        }
    }
}

