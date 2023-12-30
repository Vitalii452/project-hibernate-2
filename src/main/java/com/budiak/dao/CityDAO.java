package com.budiak.dao;

import com.budiak.model.City;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CityDAO extends AbstractDAO<City, Short> {

    public CityDAO() {
        super(City.class);
    }

    public City findMatchingCity(Session session, City city) {
        if (city.getCityName() == null || city.getCountry() == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<City> criteriaQuery = builder.createQuery(City.class);
        Root<City> root = criteriaQuery.from(City.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(builder.lower(root.get("cityName")), city.getCityName().toLowerCase()));
        predicates.add(builder.equal(root.join("country").get("countryName"), city.getCountry().getCountryName()));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<City> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
