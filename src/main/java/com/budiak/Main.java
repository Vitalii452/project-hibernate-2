package com.budiak;

import com.budiak.dao.*;
import com.budiak.model.*;
import com.budiak.service.*;
import com.budiak.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        StoreService storeService = new StoreService(new StoreDAO(), sessionFactory);
        CountryService countryService = new CountryService(new CountryDAO());
        CityService cityService = new CityService(new CityDAO(), countryService);
        AddressService addressService = new AddressService(new AddressDAO(), cityService);
        CustomerService customerService = new CustomerService(new CustomerDAO(), sessionFactory, addressService);
        RentalService rentalService = new RentalService(new RentalDAO(), customerService, sessionFactory);

        main.createCustomer(storeService, customerService);
        main.returnRental(rentalService, "Rafael", "Abney", "RAFAEL.ABNEY@sakilacustomer.org");
        main.returnRental(rentalService, "tamara", "nguyen", null);
    }

    public void createCustomer(StoreService storeService, CustomerService customerService) {
        Country country = new Country("Afghanistan");
        City city = new City("Kabul", country);

        Address customerAddress = new Address(
                "1167 Najafabad Parkway",
                "", "Kabol",
                city, "40301",
                "886649065861"
        );

        Store store = storeService.findStoreById(Byte.valueOf("1"));

        Customer customer = new Customer(
                store,
                "Vitalii45",
                "Lukov",
                "superEmail@mail.org",
                customerAddress,
                true
        );

        customerService.createCustomerInTransaction(customer);
    }

    public void returnRental(RentalService rentalService, String firstName, String lastName, String email) {
        rentalService.returnRentalInTransaction(firstName, lastName, email);
    }
}