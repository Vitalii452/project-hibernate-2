package com.budiak.dao;

import com.budiak.model.Film;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.Optional;

public class FilmDAO extends AbstractDAO<Film, Short> {

    public FilmDAO() {
        super(Film.class);
    }

    public Optional<Film> findFilmByDetails(Session session, String title, int filmYear) {
        if (title == null) {
            return Optional.empty();
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Film> criteriaQuery = builder.createQuery(Film.class);
        Root<Film> root = criteriaQuery.from(Film.class);

        Predicate filmTitlePredicate = builder.equal(root.get("title"), title);
        Predicate filmYearPredicate = builder.equal(root.get("releaseYear"), filmYear);

        criteriaQuery.select(root).where(builder.and(filmTitlePredicate, filmYearPredicate));

        TypedQuery<Film> query = session.createQuery(criteriaQuery);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
