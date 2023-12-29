package com.budiak.service;

import com.budiak.dao.CustomerDAO;
import com.budiak.model.Address;
import com.budiak.model.Customer;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.TransactionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


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

                Customer customerInDb = customerDAO.findMatchingAddress(session, customer);
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
}


