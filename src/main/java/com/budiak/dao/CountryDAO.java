package com.budiak.dao;

import com.budiak.model.Country;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CountryDAO extends AbstractDAO<Country, Long> {

    public CountryDAO() {
        super(Country.class);
    }

    public Country findMatchingCountry(Session session, Country country) {
        if (country.getCountry() == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Country> criteriaQuery = builder.createQuery(Country.class);
        Root<Country> root = criteriaQuery.from(Country.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(builder.lower(root.get("country")), country.getCountry().toLowerCase()));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Country> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
