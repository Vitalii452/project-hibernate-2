package com.budiak.service;

import com.budiak.dao.RentalDAO;
import com.budiak.model.Customer;
import com.budiak.model.Rental;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.TransactionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class RentalService {

    private static final Logger logger = LogManager.getLogger(RentalService.class);
    private final RentalDAO rentalDAO;
    private final CustomerService customerService;
    private final SessionFactory sessionFactory;

    public RentalService(RentalDAO rentalDAO, CustomerService customerService, SessionFactory sessionFactory) {
        this.rentalDAO = rentalDAO;
        this.customerService = customerService;
        this.sessionFactory = sessionFactory;
    }

    public void returnRentalInTransaction(String firstName, String lastName, String email) {
        logger.info("Initiating rental return process for customer: {} {}", firstName, lastName);
        try (Session session = sessionFactory.openSession()) {
            TransactionUtils.executeInTransaction(session, () -> {
                logger.debug("Searching for customer by details: {} {} {}", firstName, lastName, email);
                Customer customer = customerService.getCustomersByDetails(session, firstName, lastName, email);

                if (customer == null) {
                    logger.warn("Customer not found for given details: {} {} {}, proceeding without action.", firstName, lastName, email);
                    return;
                }
                logger.debug("Customer found, searching for active rental for customer ID: {}", customer.getCustomerId());
                Rental rental = rentalDAO.findRentalByCustomerId(session, customer.getCustomerId());

                if (rental == null) {
                    logger.warn("Active rental not found for customerId: {}, proceeding without action.", customer.getCustomerId());
                    return;
                }
                logger.info("Completing rental: {} for customerId: {}", rental, rental.getCustomer().getCustomerId());
                rental.completeRental();
                rentalDAO.update(session, rental);

                logger.info("Rental successfully returned for customer: {}", customer);
            });
        } catch (Exception e) {
            logger.error("Unexpected error during rental return process", e);
            throw new ServiceException("Unexpected error during rental return", e);
        }
    }
}
