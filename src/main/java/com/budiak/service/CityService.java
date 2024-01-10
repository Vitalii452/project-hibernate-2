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

    public City findOrCreateCity(Session session, City newCity) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to find or create a city: {}", newCity.getCityName());

        Country countryInDb = countryService.findOrCreateCountry(session, newCity.getCountry());
        newCity.setCountry(countryInDb);

        City existingCity = null;
        try {
            existingCity = cityDAO.findMatchingCity(session, newCity);

        } catch (Exception e) {
            LOGGER.error("Error finding city", e);
            throw new ServiceException("Error finding city", e);
        }

        if (existingCity == null) {
            try {
                LOGGER.info("Creating a new city in the database: {}", newCity.getCityName());
                cityDAO.save(session, newCity);
                return newCity;

            } catch (Exception e) {
                LOGGER.error("Error saving city: {}", e.getMessage(), e);
                throw new ServiceException("Error saving city", e);
            }
        }

        LOGGER.info("City found in the database: {}", existingCity.getCityName());
        return existingCity;
    }
}
