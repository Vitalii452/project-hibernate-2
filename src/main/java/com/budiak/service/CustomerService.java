package com.budiak.service;

import com.budiak.dao.CustomerDAO;
import com.budiak.model.Address;
import com.budiak.model.Customer;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.TransactionUtils;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;


public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    private final CustomerDAO customerDAO;

    private final SessionFactory sessionFactory;

    private final AddressService addressService;

    public CustomerService(CustomerDAO customerDAO, SessionFactory sessionFactory, AddressService addressService) {
        this.customerDAO = customerDAO;
        this.sessionFactory = sessionFactory;
        this.addressService = addressService;
    }

    public void createCustomerInTransaction(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            TransactionUtils.executeInTransaction(session, () -> {
                Address address = addressService.findOrCreateAddress(
                        session,
                        customer.getAddress().getAddress(),
                        customer.getAddress().getAddress2(),
                        customer.getAddress().getDistrict(),
                        customer.getAddress().getCity(),
                        customer.getAddress().getPostalCode(),
                        customer.getAddress().getPhone()
                );
                customer.setAddress(address);

                Customer customerInDb = customerDAO.findMatchingCustomer(session, customer);
                if (customerInDb == null) {
                    customerDAO.save(session, customer);
                    logger.info("New customer created: " + customer);
                } else {
                    logger.info("Customer already exists: " + customerInDb);
                }
            });
        } catch (Exception e) {
            logger.error("Error creating customer: " + e.getMessage(), e);
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

    public Customer getCustomersByDetails(Session session, String firstName, String lastName, String email) {
        if (session == null || !session.isOpen()) {
            logger.error("Session is closed or null");
            throw new ServiceException("Session is not open");
        }

        try {
            return customerDAO.findCustomerByDetails(session, firstName, lastName, email);
        } catch (NoResultException e) {
            logger.info("No customer found for given details: " + firstName + " " + lastName + " " + email);
            return null;
        } catch (NonUniqueResultException e) {
            logger.error("Multiple customers found for given details: " + firstName + " " + lastName + " " + email);
            throw new ServiceException("Multiple customers found", e);
        } catch (Exception e) {
            logger.error("Error finding customer: " + e.getMessage(), e);
            throw new ServiceException("Error finding customer", e);
        }
    }
}


