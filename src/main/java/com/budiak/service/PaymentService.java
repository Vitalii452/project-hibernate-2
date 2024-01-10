package com.budiak.service;

import com.budiak.dao.PaymentDAO;
import com.budiak.model.Payment;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class PaymentService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentService.class);
    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void makePayment(Session session, Payment payment) {
        HibernateUtil.validateSessionAndTransaction(session);

        LOGGER.debug("Attempting to make payment with id: {}", payment.getPaymentId());

        try {
            paymentDAO.save(session, payment);
            LOGGER.info("Payment has been successfully completed with id: {}", payment.getPaymentId());

        } catch (Exception e) {
            LOGGER.error("Failed to make payment with id: {}", payment.getPaymentId(), e);
            throw new ServiceException("Failed to make payment", e);
        }
    }
}
