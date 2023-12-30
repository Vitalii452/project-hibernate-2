package com.budiak;

import com.budiak.dao.*;
import com.budiak.model.*;
import com.budiak.service.*;
import com.budiak.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        StoreService storeService = new StoreService(new StoreDAO());
        CountryService countryService = new CountryService(new CountryDAO());
        CityService cityService = new CityService(new CityDAO(), countryService);
        AddressService addressService = new AddressService(new AddressDAO(), cityService);
        CustomerService customerService = new CustomerService(new CustomerDAO(), addressService);
        InventoryService inventoryService = new InventoryService(new InventoryDAO());
        FilmService filmService = new FilmService(new FilmDAO());
        PaymentService paymentService = new PaymentService(new PaymentDAO());
        StaffService staffService = new StaffService(new StaffDAO());
        RentalService rentalService = new RentalService(new RentalDAO(), customerService, inventoryService, filmService, staffService, paymentService);


        String firstName = "Rafael";
        String lastName = "Abney";
        String email = "RAFAEL.ABNEY@sakilacustomer.org";
        String filmTitle = "ACADEMY DINOSAUR";
        int filmYear = 2006;
        byte storeId = 1;
        byte staffId = 1;
        BigDecimal amount = new BigDecimal("19.99");

        main.createCustomer(sessionFactory, storeService, customerService);
        main.returnRental(sessionFactory, rentalService, firstName, lastName, email);
        main.processRental(sessionFactory, rentalService, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);
    }

    public void createCustomer(SessionFactory sessionFactory, StoreService storeService, CustomerService customerService) {
        Country country = new Country("Afghanistan");
        City city = new City("Kabul", country);

        Store store;
        Customer customer;

        Address customerAddress = new Address(
                "1167 Najafabad Parkway",
                "", "Kabol",
                city, "40301",
                "886649065861"
        );


        try (Session session = sessionFactory.openSession()) {
            store = storeService.findStoreById(session, Byte.valueOf("1"));
            customer = new Customer(
                    store,
                    "Abrahim45",
                    "Lukov",
                    "superEmail@mail.org",
                    customerAddress,
                    true
            );
            customerService.createCustomerInTransaction(session, customer);
        }
    }

    public void returnRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email) {
        try (Session session = sessionFactory.openSession()) {
            rentalService.returnRentalInTransaction(session, firstName, lastName, email);
        }
    }

    public void processRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email, String filmTitle, int filmYear, byte storeId, byte staffId, BigDecimal amount) {
        try (Session session = sessionFactory.openSession()) {
            rentalService.processRentalAndPaymentTransaction(session, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);
        }
    }
}