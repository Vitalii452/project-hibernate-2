package com.budiak.service;

import com.budiak.dao.AddressDAO;
import com.budiak.model.Address;
import com.budiak.model.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AddressService {

    private final AddressDAO addressDAO;

    private final CityService cityService;

    public AddressService(AddressDAO addressDAO, CityService cityService) {
        this.addressDAO = addressDAO;
        this.cityService = cityService;
    }

    public Address findOrCreateAddress(Session session, String address, String address2, String district, City city, String postalCode, String phone) {
        if (!session.getTransaction().isActive()) {
            throw new IllegalStateException("Session transaction required!");
        }

        City cityInDb = cityService.findOrCreateCity(session, city.getCity(), city.getCountry());
        Address addressObj = new Address(address, address2, district, cityInDb, postalCode, phone);
        Address addressInDB = addressDAO.findMatchingAddress(session, addressObj);

        if (addressInDB == null) {
            addressDAO.save(session, addressObj);
            return addressObj;
        }
        return addressInDB;
    }
}

