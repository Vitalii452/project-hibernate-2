package com.budiak.service;

import com.budiak.dao.CityDAO;
import com.budiak.model.City;
import com.budiak.model.Country;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CityService {

    private static final Logger logger = LogManager.getLogger(CityService.class);
    private final CityDAO cityDAO;
    private final CountryService countryService;

    public CityService(CityDAO cityDAO, CountryService countryService) {
        this.cityDAO = cityDAO;
        this.countryService = countryService;
    }

    public City findOrCreateCity(Session session, String cityName, Country country) {
        logger.debug("Attempting to find or create a city: {}", cityName);

        if (!session.getTransaction().isActive()) {
            logger.error("Session transaction not active for city: {}", cityName);
            throw new IllegalStateException("Session transaction required!");
        }

        Country countryInDb = countryService.findOrCreateCountry(session, country.getCountry());
        City cityObj = new City(cityName, countryInDb);
        City cityInBd = cityDAO.findMatchingCity(session, cityObj);

        if (cityInBd == null) {
            logger.info("Creating a new city in the database: {}", cityName);
            cityDAO.save(session, cityObj);
            return cityObj;
        }

        logger.info("City found in the database: {}", cityInBd.getCity());
        return cityInBd;
    }
}
