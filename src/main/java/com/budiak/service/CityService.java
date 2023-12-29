package com.budiak.service;

import com.budiak.dao.CityDAO;
import com.budiak.model.City;
import com.budiak.model.Country;
import org.hibernate.Session;

public class CityService {

    private final CityDAO cityDAO;

    private final CountryService countryService;

    public CityService(CityDAO cityDAO, CountryService countryService) {
        this.cityDAO = cityDAO;
        this.countryService = countryService;
    }

    public City findOrCreateCity(Session session, String city, Country country) {
        if (!session.getTransaction().isActive()) {
            throw new IllegalStateException("Session transaction required!");
        }

        Country countryInDb = countryService.findOrCreateCountry(session, country.getCountry());
        City cityObj = new City(city, countryInDb);
        City cityInBd = cityDAO.findMatchingCity(session, cityObj);

        if (cityInBd == null) {
            cityDAO.save(session, cityObj);
            return cityObj;
        }
        return cityInBd;
    }
}
