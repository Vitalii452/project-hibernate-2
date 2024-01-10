package com.budiak.config;

import com.budiak.dao.*;
import com.budiak.service.*;

public class ServiceInitializer {

    public Services initializeServices() {
        StoreService storeService = new StoreService(new StoreDAO());
        CountryService countryService = new CountryService(new CountryDAO());
        CityService cityService = new CityService(new CityDAO(), countryService);
        AddressService addressService = new AddressService(new AddressDAO(), cityService);
        CustomerService customerService = new CustomerService(new CustomerDAO(), addressService);
        InventoryService inventoryService = new InventoryService(new InventoryDAO(), storeService);
        FilmManagementService filmManagementService = new FilmManagementService(new FilmDAO(), new FilmActorDAO(), new FilmCategoryDAO(), new FilmTextDAO());
        PaymentService paymentService = new PaymentService(new PaymentDAO());
        StaffService staffService = new StaffService(new StaffDAO());
        RentalService rentalService = new RentalService(new RentalDAO(), customerService, inventoryService, filmManagementService, staffService, paymentService);
        LanguageService languageService = new LanguageService(new LanguageDAO());
        ActorService actorService = new ActorService(new ActorDAO());
        CategoryService categoryService = new CategoryService(new CategoryDAO());

        return new Services(storeService, countryService, cityService, addressService,
                customerService, inventoryService, filmManagementService,
                paymentService, staffService, rentalService,
                languageService, actorService, categoryService);
    }
}
