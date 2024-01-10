package com.budiak.service;

import com.budiak.dao.ActorDAO;
import com.budiak.model.Actor;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class ActorService {

    private static final Logger LOGGER = LogManager.getLogger(ActorService.class);
    private final ActorDAO actorDAO;

    public ActorService(ActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    /**
     * Finds an actor by their first name and last name.
     *
     * @param session the Hibernate session to use for the database transaction
     * @param firstName the first name of the actor to find
     * @param lastName the last name of the actor to find
     * @return the Actor object representing the actor with the specified first name and last name,
     *         or null if no actor is found with the specified details
     * @throws ServiceException if multiple actors are found with the specified details
     *         or if there is an error finding the actor
     */
    public Actor findActorByDetails(Session session, String firstName, String lastName) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find actor with name: {} {}", firstName, lastName);

        try {
            return actorDAO.findActorByDetails(session, firstName, lastName);

        } catch (NoResultException e) {
            LOGGER.info("No actor found with name: {} {}", firstName, lastName);
            return null;

        } catch (NonUniqueResultException e) {
            LOGGER.error("Multiple actors found for name: {} {}", firstName, lastName);
            throw new ServiceException("Multiple actors found", e);

        } catch (Exception e) {
            LOGGER.error("Error finding actor: {}", e.getMessage(), e);
            throw new ServiceException("Error finding actor", e);
        }
    }
}
