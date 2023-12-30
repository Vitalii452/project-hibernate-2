package com.budiak.dao;

import com.budiak.model.Customer;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends AbstractDAO<Customer, Short> {

    public CustomerDAO() {
        super(Customer.class);
    }

    public Customer findMatchingCustomer(Session session, Customer customer) {
        if (customer.getStore() == null || customer.getFirstName() == null ||
                customer.getLastName() == null || customer.getAddress() == null ||
                customer.getActive() == null)
            return null;

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.join("store").get("storeId"), customer.getStore().getStoreId()));

        predicates.add(builder.equal(builder.lower(root.get("firstName")), customer.getFirstName().toLowerCase()));
        predicates.add(builder.equal(builder.lower(root.get("lastName")), customer.getLastName().toLowerCase()));

        if (customer.getEmail() != null) {
            predicates.add(builder.equal(builder.lower(root.get("email")), customer.getEmail().toLowerCase()));
        }

        predicates.add(builder.equal(root.join("address").get("addressId"), customer.getAddress().getAddressId()));

        predicates.add(builder.equal(root.get("active"), customer.getActive()));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Customer> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public Customer findCustomerByDetails(Session session, String firstName, String lastName, String email) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);

        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(builder.equal(builder.lower(root.get("firstName")), firstName.toLowerCase()));
        }

        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(builder.equal(builder.lower(root.get("lastName")), lastName.toLowerCase()));
        }

        if (email != null && !email.isEmpty()) {
            predicates.add(builder.equal(builder.lower(root.get("email")), email.toLowerCase()));
        }

        criteriaQuery.select(root).where(builder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Customer> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
