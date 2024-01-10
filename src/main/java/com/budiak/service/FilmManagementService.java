package com.budiak.service;

import com.budiak.dao.*;
import com.budiak.model.*;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Optional;

public class FilmManagementService {

    private static final Logger LOGGER = LogManager.getLogger(FilmManagementService.class);
    private final FilmDAO filmDAO;
    private final FilmActorDAO filmActorDAO;
    private final FilmCategoryDAO filmCategoryDAO;
    private final FilmTextDAO filmTextDAO;

    public FilmManagementService(FilmDAO filmDAO, FilmActorDAO filmActorDAO, FilmCategoryDAO filmCategoryDAO, FilmTextDAO filmTextDAO) {
        this.filmDAO = filmDAO;
        this.filmActorDAO = filmActorDAO;
        this.filmCategoryDAO = filmCategoryDAO;
        this.filmTextDAO = filmTextDAO;
    }

    /**
     * Finds an entity by its ID using the provided DAO and session.
     *
     * @param <T>     the type of the entity
     * @param <ID>    the type of the entity ID
     * @param dao     the DAO used to interact with the entity
     * @param session the session used for the database transaction
     * @param id      the ID of the entity
     * @return the entity with the given ID, or null if not found
     * @throws ServiceException if an error occurs while finding the entity
     */
    private <T, ID extends Serializable> T findEntityById(AbstractDAO<T, ID> dao, Session session, ID id) {
        T entity;
        try {
            entity = dao.findById(session, id);
        } catch (Exception e) {
            LOGGER.error("Error occurred while finding entity with ID: {}", id, e);
            throw new ServiceException("Error occurred while finding entity", e);
        }
        return entity;
    }

    /**
     * Finds a Film by its title and release year.
     *
     * @param session    the session used for the database transaction
     * @param title      the title of the Film to search for
     * @param filmYear   the release year of the Film to search for
     * @return the Film with the given title and release year, or null if not found
     * @throws ServiceException if an error occurs while finding the Film
     */
    public Film findFilmByTitleAndYear(Session session, String title, int filmYear) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find film with title and year: {} {}", title, filmYear);

        try {
            return filmDAO.findFilmByDetails(session, title, filmYear).orElse(null);

        } catch (NonUniqueResultException e) {
            LOGGER.error("Multiple films found for given title and year: {} {}", title, filmYear);
            throw new ServiceException("Multiple films found", e);

        } catch (Exception e) {
            LOGGER.error("Error finding film: {}", e.getMessage(), e);
            throw new ServiceException("Error finding film", e);
        }
    }

    /**
     * Adds a new film to the database.
     *
     * @param session the session used for the database transaction
     * @param film    the film to be added
     * @return an Optional containing the added film, or an empty Optional if the film already exists
     * @throws ServiceException if an error occurs while adding the film
     */
    public Optional<Film> addNewFilm(Session session, Film film) {
        HibernateUtil.validateSessionAndTransaction(session);

         LOGGER.debug("Attempting to create film with title and year: {} {}", film.getTitle(), film.getReleaseYear());

        Optional<Film> existingFilm = filmDAO.findFilmByDetails(session, film.getTitle(), film.getReleaseYear());

        if (existingFilm.isEmpty()) {
            filmDAO.save(session, film);
            LOGGER.info("Film successfully created with title: {}", film.getTitle());
            return Optional.of(film);

        } else {
            LOGGER.info("Film already exists with id: {}, title: {}, releaseYear: {}",
                    existingFilm.get().getFilmId(), existingFilm.get().getTitle(), existingFilm.get().getReleaseYear());
            return Optional.empty();
        }
    }

    /**
     * Creates a new association between an actor and a film in the database.
     *
     * @param session the session used for the database transaction
     * @param actor   the actor involved in the association
     * @param film    the film involved in the association
     */
    public void createFilmActorAssociation(Session session, Actor actor, Film film) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to create association between actorId and filmId : {} {}", actor.getActorId(), film.getFilmId());

        FilmActor.Id id = new FilmActor.Id(actor.getActorId(), film.getFilmId());

        if (findEntityById(filmActorDAO, session, id) != null) {
            return;
        }

        LOGGER.info("Creating a new FilmActor in the database");
        FilmActor newFilmActor = new FilmActor(actor, film);
        filmActorDAO.save(session, newFilmActor);
    }

    /**
     * Creates a new association between a film and a category in the database.
     *
     * @param session  the session used for the database transaction
     * @param category the category involved in the association
     * @param film     the film involved in the association
     */
    public void createFilmCategoryAssociation(Session session, Category category, Film film) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to create association between categoryId and filmId : {} {}", category.getCategoryId(), film.getFilmId());

        FilmCategory.Id id = new FilmCategory.Id(film.getFilmId(), category.getCategoryId());

        if (findEntityById(filmCategoryDAO, session, id) != null) {
            return;
        }

        LOGGER.info("Creating a new FilmCategory in the database");
        FilmCategory newFilmCategory = new FilmCategory(film, category);
        filmCategoryDAO.save(session, newFilmCategory);
    }

    /**
     * Creates a new association between a film and its text description in the database.
     *
     * @param session     the session used for the database transaction
     * @param film        the film object representing the film to create the association for
     * @param description the text description of the film
     */
    public void createFilmTextAssociation(Session session, Film film, String description) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to create film_text column with filmId : {}", film.getFilmId());

        if (findEntityById(filmTextDAO, session, film.getFilmId()) != null) {
            return;
        }

        LOGGER.info("Creating a new FilmText in the database");
        FilmText newFilmText = new FilmText(film, film.getTitle(), description);
        filmTextDAO.save(session, newFilmText);
    }
}
