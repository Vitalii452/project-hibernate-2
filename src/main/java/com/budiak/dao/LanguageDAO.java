package com.budiak.dao;

import com.budiak.model.Language;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

public class LanguageDAO extends AbstractDAO<Language, Byte> {

    public LanguageDAO() {
        super(Language.class);
    }

    public Language findInventoryByName(Session session, String languageName) {
        if (languageName == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Language> criteriaQuery = builder.createQuery(Language.class);
        Root<Language> root = criteriaQuery.from(Language.class);

        Predicate languageNamePredicate = builder.equal(builder.lower(root.get("name")), languageName);

        criteriaQuery.select(root).where(languageNamePredicate);

        TypedQuery<Language> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
