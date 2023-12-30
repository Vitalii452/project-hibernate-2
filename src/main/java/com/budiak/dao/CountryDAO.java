package com.budiak.dao;

import com.budiak.model.Country;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

public class CountryDAO extends AbstractDAO<Country, Long> {

    public CountryDAO() {
        super(Country.class);
    }

    public Country findMatchingCountry(Session session, Country country) {
        if (country.getCountryName() == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Country> criteriaQuery = builder.createQuery(Country.class);
        Root<Country> root = criteriaQuery.from(Country.class);

        Predicate countryPredicate = builder.equal(builder.lower(root.get("countryName")), country.getCountryName().toLowerCase());

        criteriaQuery.select(root).where(countryPredicate);

        TypedQuery<Country> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
