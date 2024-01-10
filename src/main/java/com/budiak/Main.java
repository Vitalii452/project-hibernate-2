package com.budiak;

import com.budiak.config.ServiceInitializer;
import com.budiak.config.Services;
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

        // Service Initialization
        ServiceInitializer serviceInitializer = new ServiceInitializer();
        Services services = serviceInitializer.initializeServices();

        // Retrieving service instances
        StoreService storeService = services.getStoreService();
        CustomerService customerService = services.getCustomerService();
        InventoryService inventoryService = services.getInventoryService();
        FilmManagementService filmManagementService = services.getFilmManagementService();
        RentalService rentalService = services.getRentalService();
        LanguageService languageService = services.getLanguageService();
        ActorService actorService = services.getActorService();
        CategoryService categoryService = services.getCategoryService();

        // Preparing data for demonstrating method functionalities
        String firstName = "Rafael";
        String lastName = "Abney";
        String email = "RAFAEL.ABNEY@sakilacustomer.org";
        String filmTitle = "ACADEMY DINOSAUR";
        int filmYear = 2006;
        byte storeId = 1;
        byte staffId = 1;
        BigDecimal amount = new BigDecimal("19.99");
        Country country = new Country("Afghanistan");
        City city = new City("Kabul", country);
        Address customerAddress = generateAddress(city);
        Customer customer = generateCustomer(customerAddress);
        Film film = generateFilm();
        String filmDescription = "You have not seen this before!";

        Session session = sessionFactory.openSession();
        List<Actor> actors = List.of(actorService.findActorByDetails(session, "PENELOPE", "GUINESS"));
        List<Category> categories = List.of(categoryService.findCategoryByName(session, "Action"));
        session.close();

        // Invoking methods to demonstrate functionality
        main.createCustomer(sessionFactory, storeService, customerService, customer, storeId);
        main.returnRental(sessionFactory, rentalService, firstName, lastName, email);
        main.createRental(sessionFactory, rentalService, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);
        main.addNewFilmAndMakeAvailableForRental(sessionFactory, film, filmManagementService, languageService, inventoryService, actors, categories, filmDescription, storeId);
    }

    private static Film generateFilm() {
        return new Film.Builder()
                .setTitle("Matryoshka 7")
                .setDescription("Matryoshka saves the world once again!")
                .setReleaseYear(2023)
                .setLength((short) 2023)
                .setSpecialFeatures(Set.of("Trailers", "Commentaries"))
                .build();
    }

    private static Customer generateCustomer(Address customerAddress) {
        return new Customer.Builder()
                .setFirstName("Abrahim45")
                .setLastName("Lukov")
                .setEmail("superEmail@mail.org")
                .setAddress(customerAddress)
                .setActive(true)
                .build();
    }

    private static Address generateAddress(City city) {
        return new Address.Builder()
                .setAddress("1167 Najafabad Parkway")
                .setAddress2("")
                .setDistrict("Kabol")
                .setCity(city)
                .setPostalCode("40301")
                .setPhone("886649065861")
                .build();
    }


    /**
     * Creates a new customer by associating it with a store. The customer is saved in the database within a transaction.
     *
     * @param sessionFactory The Hibernate session factory.
     * @param storeService The service for accessing and manipulating store data.
     * @param customerService The service for creating and retrieving customer data.
     * @param customer The customer object to be created.
     * @param storeId The ID of the store to associate the customer with.
     */
    public void createCustomer(SessionFactory sessionFactory, StoreService storeService, CustomerService customerService, Customer customer, Byte storeId) {
        LOGGER.info("----Start of the createCustomer method----");

        try (Session session = sessionFactory.openSession()) {
            Store store = storeService.findStoreById(session, storeId);
            customer.setStore(store);
            customerService.createCustomerInTransaction(session, customer);
        }
        LOGGER.info("----End of the createCustomer method----");
    }

    /**
     * Processes a rental return in a transaction.
     *
     * @param sessionFactory   The Hibernate session factory.
     * @param rentalService    The rental service.
     * @param firstName        The first name of the customer.
     * @param lastName         The last name of the customer.
     * @param email            The email address of the customer.
     */
    public void returnRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email) {
        LOGGER.info("----Start of the returnRental method----");
        try (Session session = sessionFactory.openSession()) {
            rentalService.returnRentalInTransaction(session, firstName, lastName, email);
        }
        LOGGER.info("----End of the returnRental method----");
    }

    /**
     * Creates a new rental in transaction.
     *
     * @param sessionFactory The Hibernate session factory.
     * @param rentalService The rental service.
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @param email The email address of the customer.
     * @param filmTitle The title of the film.
     * @param filmYear The year of the film.
     * @param storeId The ID of the store where the rental takes place.
     * @param staffId The ID of the staff handling the rental.
     * @param amount The payment amount for the rental.
     */
    public void createRental(SessionFactory sessionFactory, RentalService rentalService, String firstName, String lastName, String email, String filmTitle, int filmYear, byte storeId, byte staffId, BigDecimal amount) {
        LOGGER.info("----Start of the processRental method----");
        try (Session session = sessionFactory.openSession()) {
            rentalService.processRentalInTransaction(session, firstName, lastName, email, filmTitle, filmYear, storeId, staffId, amount);
        }
        LOGGER.info("----End of the processRental method----");
    }

    /**
     * Adds a new film and makes it available for rental.
     *
     * @param sessionFactory The Hibernate session factory.
     * @param film The film object to be added.
     * @param filmManagementService The service for managing film data.
     * @param languageService The service for managing language data.
     * @param inventoryService The service for managing inventory data.
     * @param actors The list of actors associated with the film.
     * @param categories The list of categories associated with the film.
     * @param description The description of the film.
     * @param storeId The ID of the store where the film will be available for rental.
     */
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

    /**
     * Associates various elements of a film, such as actors, categories, description, and inventory,
     * with the film itself in a database session. This method is private and called internally
     * within the class.
     *
     * @param session                The current Hibernate session.
     * @param filmManagementService  The service for managing film data.
     * @param inventoryService       The service for managing inventory data.
     * @param actors                 The list of actors associated with the film.
     * @param categories             The list of categories associated with the film.
     * @param description            The description of the film.
     * @param film                   The film object to be associated with various elements.
     * @param storeId                The ID of the store where the film will be available for rental.
     */
    private void associateFilmElements(Session session, FilmManagementService filmManagementService,
                                       InventoryService inventoryService, List<Actor> actors, List<Category> categories,
                                       String description, Film film, Byte storeId) {
        actors.forEach(actor -> filmManagementService.createFilmActorAssociation(session, actor, film));
        categories.forEach(category -> filmManagementService.createFilmCategoryAssociation(session, category, film));
        filmManagementService.createFilmTextAssociation(session, film, description);
        inventoryService.addFilmToInventory(session, film, storeId);
    }
}