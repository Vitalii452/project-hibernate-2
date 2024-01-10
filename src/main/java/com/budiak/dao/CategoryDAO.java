package com.budiak.dao;

import com.budiak.model.Category;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

public class CategoryDAO extends AbstractDAO<Category, Byte> {

    public CategoryDAO() {
        super(Category.class);
    }

    public Category findMatchingCategory(Session session, String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);
        Root<Category> root = criteriaQuery.from(Category.class);

        Predicate categoryNamePredicate = builder.equal(root.get("categoryName"), categoryName);

        criteriaQuery.select(root).where(categoryNamePredicate);

        TypedQuery<Category> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
