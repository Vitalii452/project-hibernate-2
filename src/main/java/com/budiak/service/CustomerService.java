package com.budiak.service;

import com.budiak.dao.CustomerDAO;
import com.budiak.model.Address;
import com.budiak.model.Customer;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import com.budiak.util.TransactionUtils;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;


public class CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CustomerDAO customerDAO;
    private final AddressService addressService;

    public CustomerService(CustomerDAO customerDAO, AddressService addressService) {
        this.customerDAO = customerDAO;
        this.addressService = addressService;
    }

    public void createCustomerInTransaction(Session session, Customer customer) {
        HibernateUtil.validateSession(session);

        try {
            TransactionUtils.executeInTransaction(session, () -> {
                Address address = addressService.findOrCreateAddress(session,customer.getAddress());
                customer.setAddress(address);

                Customer existingCustomer = customerDAO.findMatchingCustomer(session, customer);
                if (existingCustomer == null) {
                    customerDAO.save(session, customer);
                    LOGGER.info("New customer created: {}", customer);
                } else {
                    LOGGER.info("Customer already exists: {}", existingCustomer);
                }
            });

        } catch (Exception e) {
            LOGGER.error("Error creating customer: {}", e.getMessage(), e);
            throw new ServiceException("Unable to create customer", e);
        }
    }

    /**
     * Find a customer by first name, last name, and email. This method assumes
     * that the session is already managed at the level of the calling service.
     * It returns the single customer matching the criteria or null if no customer is found.
     * Throws ServiceException if multiple customers are found or in case of other errors.
     *
     * @param session   the Hibernate session
     * @param firstName the first name of the customer
     * @param lastName  the last name of the customer
     * @param email     the email address of the customer
     * @return a customer matching the criteria, or null if no matching customer is found
     * @throws ServiceException if multiple customers are found or in case of other errors
     */

    public Customer getCustomerByDetails(Session session, String firstName, String lastName, String email) {
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to get customer by details: {} {} {}", firstName, lastName, email);

        try {
            return customerDAO.findCustomerByDetails(session, firstName, lastName, email);

        } catch (NoResultException e) {
            LOGGER.info("No customer found for given details: {} {} {}", firstName, lastName, email);
            return null;

        } catch (NonUniqueResultException e) {
            LOGGER.error("Multiple customers found for given details: {} {} {}", firstName, lastName, email);
            throw new ServiceException("Multiple customers found", e);

        } catch (Exception e) {
            LOGGER.error("Error finding customer: {}", e.getMessage(), e);
            throw new ServiceException("Error finding customer", e);
        }
    }
}


