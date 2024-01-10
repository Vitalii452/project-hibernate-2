## Project Overview

This project involves the development of transactional methods for a movie rental service, providing a practical demonstration of database-driven operations and ensuring data integrity. Key features and their corresponding methods are outlined below:

### Features and Methods

1. **Transactional Customer Creation (`createCustomer`)**:
   - A method to add a new customer to the `customer` table, ensuring all related fields are populated and transactional integrity is maintained.
   - Prevents scenarios where only partial data is inserted into the database.

2. **Film Rental Return (`returnRental`)**:
   - A transactional method for handling the event where a customer returns a previously rented film.

3. **Film Rental and Payment Processing (`createRental`)**:
   - Implements the process where a customer rents an inventory item from a store and makes a payment.
   - Ensures the film is available for rent by checking the `rental` table for appropriate conditions.

4. **New Film Release and Availability (`addNewFilmAndMakeAvailableForRental`)**:
   - Manages the addition of a new film to the system, making it available for rental.
   - Includes setting various film attributes such as language, actors, and categories.

Each method demonstrates a specific functionality within the context of a movie rental business, highlighting the use of transactional operations to maintain database consistency and integrity.
