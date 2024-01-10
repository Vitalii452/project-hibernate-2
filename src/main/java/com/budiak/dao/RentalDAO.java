package com.budiak.dao;

import com.budiak.model.Rental;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RentalDAO extends AbstractDAO<Rental, Long> {

    public RentalDAO() {
        super(Rental.class);
    }

    public Rental findRentalByCustomerId(Session session, short customerId) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Rental> criteriaQuery = builder.createQuery(Rental.class);
        Root<Rental> root = criteriaQuery.from(Rental.class);

        Predicate customerPredicate = builder.equal(root.join("customer").get("customerId"), customerId);
        Predicate returnDatePredicate = builder.isNull(root.get("returnDate"));

        criteriaQuery.select(root).where(builder.and(customerPredicate, returnDatePredicate));

        Query<Rental> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
