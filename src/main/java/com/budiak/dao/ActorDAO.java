package com.budiak.dao;

import com.budiak.model.Actor;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

public class ActorDAO extends AbstractDAO<Actor, Short> {

    public ActorDAO() {
        super(Actor.class);
    }

    public Actor findActorByDetails(Session session, String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = builder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);

        Predicate firstNamePredicate = builder.equal(root.get("firstName"), firstName);
        Predicate lastNamePredicate = builder.equal(root.get("lastName"), lastName);

        criteriaQuery.select(root).where(builder.and(firstNamePredicate, lastNamePredicate));

        TypedQuery<Actor> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
