package com.budiak;

import com.budiak.config.ServiceInitializer;
import com.budiak.config.Services;
import com.budiak.dao.*;
import com.budiak.model.*;
import com.budiak.service.*;
import com.budiak.util.HibernateUtil;
import com.budiak.util.TransactionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Main main = new Main();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        ServiceInitializer serviceInitializer = new ServiceInitializer();
        Services services = serviceInitializer.initializeServices();

        StoreService storeService = services.getStoreService();
        CustomerService customerService = services.getCustomerService();
        InventoryService inventoryService = services.getInventoryService();
        FilmManagementService filmManagementService = services.getFilmManagementService();
        RentalService rentalService = services.getRentalService();
        LanguageService languageService = services.getLanguageService();
        ActorService actorService = services.getActorService();
        CategoryService categoryService = services.getCategoryService();


        String firstName = "Rafael";
        String lastName = "Abney";
        String email = "RAFAEL.ABNEY@sakilacustomer.org";
        String filmTitle = "ACADEMY DINOSAUR";
        int filmYear = 2006;
        byte storeId = 1;
        byte staffId = 1;
        BigDecimal amount = new BigDecimal("19.99");
        Film film = new Film.Builder()
                .setTitle("Matryoshka 7")
                .setDescription("Matryoshka saves the world once again!")
                .setReleaseYear(2023)
                .setLength((short) 2023)
                .setSpecialFeatures(Set.of("Trailers", "Commentaries"))
                .build();
        String filmDescription = "You have not seen this before!";

        Session session = sessionFactory.openSession();

        List<Actor> actors = List.of(actorService.findActorByDetails(session, "PENELOPE", "GUINESS"));
        List<Category> categories = List.of(categoryService.findCategoryByName(session, "Action"));

        session.close();

        main.createCustomer(sessionFactory, storeService, customerService);
        main.returnRental(sessionFactory, rentalService, firstName, lastName, email);
        main.createRental(sessionFactory, rentalService, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);


        main.addNewFilmAndMakeAvailableForRental(sessionFactory, film, filmManagementService, languageService, inventoryService, actors, categories, filmDescription, storeId);
    }

    public void createCustomer(SessionFactory sessionFactory, StoreService storeService, CustomerService customerService) {
        LOGGER.info("----Start of the createCustomer method----");
        Country country = new Country("Afghanistan");
        City city = new City("Kabul", country);

        Store store;
        Customer customer;

        Address customerAddress = new Address.Builder()
                .setAddress("1167 Najafabad Parkway")
                .setAddress2("")
                .setDistrict("Kabol")
                .setCity(city)
                .setPostalCode("40301")
                .setPhone("886649065861")
                .build();


        try (Session session = sessionFactory.openSession()) {
            store = storeService.findStoreById(session, (byte) 1);

            customer = new Customer.Builder()
                    .setStore(store)
                    .setFirstName("Abrahim45")
                    .setLastName("Lukov")
                    .setEmail("superEmail@mail.org")
                    .setAddress(customerAddress)
                    .setActive(true)
                    .build();

            customerService.createCustomerInTransaction(session, customer);
        }
        LOGGER.info("----End of the createCustomer method----");
    }

    public void returnRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email) {
        LOGGER.info("----Start of the returnRental method----");
        try (Session session = sessionFactory.openSession()) {
            rentalService.returnRentalInTransaction(session, firstName, lastName, email);
        }
        LOGGER.info("----End of the returnRental method----");
    }

    public void createRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email, String filmTitle, int filmYear, byte storeId, byte staffId, BigDecimal amount) {
        LOGGER.info("----Start of the processRental method----");
        try (Session session = sessionFactory.openSession()) {
            rentalService.processRentalAndPaymentTransaction(session, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);
        }
        LOGGER.info("----End of the processRental method----");
    }

    public void addNewFilmAndMakeAvailableForRental(SessionFactory sessionFactory, Film film,
                                                    FilmManagementService filmManagementService, LanguageService languageService,
                                                    InventoryService inventoryService,
                                                    List<Actor> actors, List<Category> categories, String description, Byte storeId) {
        LOGGER.info("----Start of the addNewFilmAndMakeAvailableForRental method----");

        try (Session session = sessionFactory.openSession()) {
            film.setLanguage(languageService.findLanguageByName(session, "English"));
            film.setOriginalLanguage(languageService.findLanguageByName(session, "english"));

            TransactionUtils.executeInTransaction(session, () -> {
                Optional<Film> existingFilm = filmManagementService.addNewFilm(session, film);
                if (existingFilm.isPresent()) {
                    associateFilmElements(session, filmManagementService, inventoryService, actors, categories, description, film, storeId);
                }
            });
        }

        LOGGER.info("----End of the addNewFilmAndMakeAvailableForRental method----");
    }

    private void associateFilmElements(Session session, FilmManagementService filmManagementService,
                                       InventoryService inventoryService, List<Actor> actors, List<Category> categories,
                                       String description, Film film, Byte storeId) {
        actors.forEach(actor -> filmManagementService.createFilmActorAssociation(session, actor, film));
        categories.forEach(category -> filmManagementService.createFilmCategoryAssociation(session, category, film));
        filmManagementService.createFilmTextAssociation(session, film, description);
        inventoryService.addFilmToInventory(session, film, storeId);
    }
}