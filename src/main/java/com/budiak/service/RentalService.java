package com.budiak.service;

import com.budiak.dao.RentalDAO;
import com.budiak.model.*;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import com.budiak.util.TransactionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalService {

    private static final Logger LOGGER = LogManager.getLogger(RentalService.class);
    private final RentalDAO rentalDAO;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final FilmManagementService filmManagementService;
    private final StaffService staffService;
    private final PaymentService paymentService;

    public RentalService(RentalDAO rentalDAO, CustomerService customerService, InventoryService inventoryService, FilmManagementService filmManagementService, StaffService staffService, PaymentService paymentService) {
        this.rentalDAO = rentalDAO;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.filmManagementService = filmManagementService;
        this.staffService = staffService;
        this.paymentService = paymentService;
    }

    public void returnRentalInTransaction(Session session, String firstName, String lastName, String email) {
        HibernateUtil.validateSession(session);

        LOGGER.info("Initiating rental return process for customer: {} {}", firstName, lastName);
        try {
            TransactionUtils.executeInTransaction(session, () -> {
                LOGGER.debug("Searching for customer by details: {} {} {}", firstName, lastName, email);
                Customer customer = customerService.getCustomerByDetails(session, firstName, lastName, email);

                if (customer == null) {
                    LOGGER.warn("Customer not found for given details: {} {} {}, proceeding without action.", firstName, lastName, email);
                    return;
                }
                LOGGER.debug("Customer found, searching for active rental for customer ID: {}", customer.getCustomerId());
                Rental rental = rentalDAO.findRentalByCustomerId(session, customer.getCustomerId());

                if (rental == null) {
                    LOGGER.warn("Active rental not found for customerId: {}, proceeding without action.", customer.getCustomerId());
                    return;
                }
                LOGGER.info("Completing rental: {} for customerId: {}", rental, rental.getCustomer().getCustomerId());

                rental.completeRental();
                rentalDAO.update(session, rental);

                LOGGER.info("Rental successfully returned for customer: {}", customer);
            });
        } catch (Exception e) {
            LOGGER.error("Unexpected error during rental return process", e);
            throw new ServiceException("Unexpected error during rental return", e);
        }
    }

    public void processRentalAndPaymentTransaction(Session session, String firstName, String lastName, String email,
                                                   String filmTitle, int filmYear, byte storeId, byte staffId, BigDecimal amount) {
        HibernateUtil.validateSession(session);

        try {
            TransactionUtils.executeInTransaction(session, () -> {
                Customer customer = customerService.getCustomerByDetails(session, firstName, lastName, email);
                Film film = filmManagementService.findFilmByTitleAndYear(session, filmTitle, filmYear);
                Staff staff = staffService.findStaffById(session, staffId);

                Inventory inventory = inventoryService.findAvailableInventoryByDetails(session, film.getFilmId(), storeId);
                Rental rental = createRental(inventory, customer, staff);
                rentalDAO.save(session, rental);

                Payment payment = createPayment(customer, staff, rental, amount);
                paymentService.makePayment(session, payment);

                LOGGER.info("Rental transaction completed successfully for customer: {}", customer.getCustomerId());
            });
        } catch (Exception e) {
            LOGGER.error("Rental transaction failed: {}", e.getMessage(), e);
            throw new ServiceException("Unexpected error during rental process", e);
        }
    }

    private Rental createRental(Inventory inventory, Customer customer, Staff staff) {
        return new Rental(LocalDateTime.now(), inventory, customer, null, staff);
    }

    private Payment createPayment(Customer customer, Staff staff, Rental rental, BigDecimal amount) {
        return new Payment(customer, staff, rental, LocalDateTime.now(), amount);
    }
}
