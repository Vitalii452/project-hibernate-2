package com.budiak.dao;

import com.budiak.model.Inventory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

public class InventoryDAO extends AbstractDAO<Inventory, Integer> {

    public InventoryDAO() {
        super(Inventory.class);
    }

    public Inventory findInventoryByFilmAndStoreId(Session session, short filmId, byte storeId) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Inventory> criteriaQuery = builder.createQuery(Inventory.class);
        Root<Inventory> root = criteriaQuery.from(Inventory.class);

        Predicate filmdIdPredicate = builder.equal(root.join("film").get("filmId"), filmId);
        Predicate storeIdPredicate = builder.equal(root.join("store").get("storeId"), storeId);

        criteriaQuery.select(root).where(builder.and(filmdIdPredicate, storeIdPredicate));

        TypedQuery<Inventory> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
