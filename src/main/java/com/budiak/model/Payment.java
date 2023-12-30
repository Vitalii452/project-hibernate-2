package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(schema = "movie", name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private Short paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(name = "amount", nullable = false, precision = 5, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public Payment() {
    }

    public Payment(Customer customer, Staff staff, Rental rental, LocalDateTime paymentDate, BigDecimal amount) {
        this.customer = customer;
        this.staff = staff;
        this.rental = rental;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Short getPaymentId() {
        return paymentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
