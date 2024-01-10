package com.budiak.config;

import com.budiak.service.*;

public class Services {
    private final StoreService storeService;
    private final CountryService countryService;
    private final CityService cityService;
    private final AddressService addressService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final FilmManagementService filmManagementService;
    private final PaymentService paymentService;
    private final StaffService staffService;
    private final RentalService rentalService;
    private final LanguageService languageService;
    private final ActorService actorService;
    private final CategoryService categoryService;

    public Services(StoreService storeService, CountryService countryService, CityService cityService, AddressService addressService, CustomerService customerService, InventoryService inventoryService, FilmManagementService filmManagementService, PaymentService paymentService, StaffService staffService, RentalService rentalService, LanguageService languageService, ActorService actorService, CategoryService categoryService) {
        this.storeService = storeService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.filmManagementService = filmManagementService;
        this.paymentService = paymentService;
        this.staffService = staffService;
        this.rentalService = rentalService;
        this.languageService = languageService;
        this.actorService = actorService;
        this.categoryService = categoryService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public CountryService getCountryService() {
        return countryService;
    }

    public CityService getCityService() {
        return cityService;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public FilmManagementService getFilmManagementService() {
        return filmManagementService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public RentalService getRentalService() {
        return rentalService;
    }

    public LanguageService getLanguageService() {
        return languageService;
    }

    public ActorService getActorService() {
        return actorService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }
}
