package com.budiak.service;

import com.budiak.dao.CityDAO;
import com.budiak.model.City;
import com.budiak.model.Country;
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

    public City findOrCreateCity(Session session, String cityName, Country country) {
        if (session == null || cityName == null || country == null ||
                cityName.isEmpty()) {
            throw new IllegalArgumentException("Session, cityName, and country must not be null");
        }

        LOGGER.debug("Attempting to find or create a city: {}", cityName);

        if (!session.getTransaction().isActive()) {
            LOGGER.error("Session transaction not active for city: {}", cityName);
            throw new IllegalStateException("Session transaction required!");
        }

        Country countryInDb = countryService.findOrCreateCountry(session, country.getCountryName());
        City newCity = new City(cityName, countryInDb);
        City existingCity = cityDAO.findMatchingCity(session, newCity);

        if (existingCity == null) {
            LOGGER.info("Creating a new city in the database: {}", cityName);
            cityDAO.save(session, newCity);
            return newCity;
        }

        LOGGER.info("City found in the database: {}", existingCity.getCityName());
        return existingCity;
    }
}
