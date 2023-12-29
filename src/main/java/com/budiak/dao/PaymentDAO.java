package com.budiak.dao;

import com.budiak.model.Payment;

public class PaymentDAO extends AbstractDAO<Payment, Short> {

    public PaymentDAO() {
        super(Payment.class);
    }
}
