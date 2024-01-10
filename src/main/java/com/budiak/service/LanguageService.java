package com.budiak.service;

import com.budiak.dao.LanguageDAO;
import com.budiak.model.Language;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class LanguageService {

    private static final Logger LOGGER = LogManager.getLogger(LanguageService.class);
    private final LanguageDAO languageDAO;

    public LanguageService(LanguageDAO languageDAO) {
        this.languageDAO = languageDAO;
    }

    /**
     * Finds a Language object in the database based on the language name.
     *
     * @param session      the Hibernate session object.
     * @param languageName the name of the language to search for.
     * @return the Language object if found, or null if not found.
     * @throws ServiceException if there are multiple languages found for the given name, or if there is an error finding the language.
     */
    public Language findLanguageByName(Session session, String languageName) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find language with name: {}", languageName);

        try {
            return languageDAO.findInventoryByName(session, languageName);

        } catch (NoResultException e) {
            LOGGER.info("No language found with name: {}", languageName);
            return null;

        } catch (NonUniqueResultException e) {
            LOGGER.error("Multiple languages found for given name: {}", languageName);
            throw new ServiceException("Multiple languages found", e);

        } catch (Exception e) {
            LOGGER.error("Error finding language: {}", e.getMessage(), e);
            throw new ServiceException("Error finding language", e);
        }
    }
}
