package com.budiak.service;

import com.budiak.dao.CustomerDAO;
import com.budiak.model.Address;
import com.budiak.model.Customer;
import com.budiak.util.TransactionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class CustomerService {

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

                Customer customerInBd = customerDAO.findMatchingAddress(session, customer);
                if (customerInBd == null) {
                    customerDAO.save(session, customer);
                }
            });
        } catch (Exception e) {
            // EXCEPTION HANDLING
        }
    }
}


