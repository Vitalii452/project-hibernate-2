package com.budiak.dao;

import com.budiak.model.Rental;

public class RentalDAO extends AbstractDAO<Rental, Long> {

    public RentalDAO() {
        super(Rental.class);
    }
}
