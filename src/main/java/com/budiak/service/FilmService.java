package com.budiak.service;

import com.budiak.dao.FilmDAO;
import com.budiak.model.Film;
import com.budiak.service.Exception.ServiceException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class FilmService {

    private static final Logger LOGGER = LogManager.getLogger(FilmService.class);
    private final FilmDAO filmDAO;

    public FilmService(FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }


    public Film findFilmByTitleAndYear(Session session, String title, int filmYear) {
        LOGGER.debug("Attempting to find film with title and year: {} {}", title, filmYear);

        if (session == null || !session.isOpen()) {
            LOGGER.error("Session is closed or null for title: {} and year: {}", title, filmYear);
            throw new ServiceException("Session is not open");
        }

        try {
            return filmDAO.findMatchFilmByTitleAndYear(session, title, filmYear);
        } catch (NoResultException e) {
            LOGGER.info("No film found for given title and year: {} {}", title, filmYear);
            return null;
        } catch (NonUniqueResultException e) {
            LOGGER.error("Multiple films found for given title and year: {} {}", title, filmYear);
            throw new ServiceException("Multiple films found", e);
        } catch (Exception e) {
            LOGGER.error("Error finding film: {}", e.getMessage(), e);
            throw new ServiceException("Error finding film", e);
        }
    }
}
