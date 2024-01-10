package com.budiak.service;

import com.budiak.dao.CategoryDAO;
import com.budiak.model.Category;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CategoryService {

    private static final Logger LOGGER = LogManager.getLogger(CategoryService.class);
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    /**
     * Finds a category by its name.
     *
     * @param session      The session to perform the database operations.
     * @param categoryName The name of the category to search for.
     * @return The category with the specified name if found, null if no category is found with the given name.
     * @throws ServiceException If an error occurs while finding the category.
     */
    public Category findCategoryByName(Session session, String categoryName) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find category with name: {}", categoryName);

        Category category = null;
        try {
            category = categoryDAO.findMatchingCategory(session, categoryName);
            return category;

        } catch (NoResultException e) {
            LOGGER.info("No category found with name: {}", categoryName);
            return null;

        } catch (Exception e) {
            LOGGER.error("Error finding category", e);
            throw new ServiceException("Error finding category", e);
        }
    }
}
