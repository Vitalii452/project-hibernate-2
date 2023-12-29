package com.budiak.service;

import com.budiak.dao.CountryDAO;
import com.budiak.model.Country;
import org.hibernate.Session;

public class CountryService {

    private final CountryDAO countryDAO;

    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public Country findOrCreateCountry(Session session, String country) {
        if (!session.getTransaction().isActive()) {
            throw new IllegalStateException("Session transaction required!");
        }

        Country countryObj = new Country(country);
        Country countryInBd = countryDAO.findMatchingCountry(session, countryObj);

        if (countryInBd == null) {
            countryDAO.save(session, countryObj);
            return countryObj;
        }
        return countryInBd;
    }
}
