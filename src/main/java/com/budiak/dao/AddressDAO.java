package com.budiak.dao;

import com.budiak.model.Address;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class AddressDAO extends AbstractDAO<Address, Short> {

    public AddressDAO() {
        super(Address.class);
    }

    public Address findMatchingAddress(Session session, Address address) {
        if (address.getAddress() == null || address.getCity() == null ||
                address.getDistrict() == null || address.getPhone() == null) {
            return null;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Address> criteriaQuery = builder.createQuery(Address.class);
        Root<Address> root = criteriaQuery.from(Address.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(builder.lower(root.get("address")), address.getAddress().toLowerCase()));

        if (address.getAddress2() != null) {
            predicates.add(builder.equal(builder.lower(root.get("address2")), address.getAddress2().toLowerCase()));
        }

        predicates.add(builder.equal(builder.lower(root.get("district")), address.getDistrict().toLowerCase()));

        predicates.add(builder.equal(root.join("city").get("cityName"), address.getCity().getCityName()));
        predicates.add(builder.equal(root.join("city").join("country").get("countryName"), address.getCity().getCountry().getCountryName()));

        if (address.getPostalCode() != null) {
            predicates.add(builder.equal(root.get("postalCode"), address.getPostalCode()));
        }

        predicates.add(builder.equal(root.get("phone"), address.getPhone()));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Address> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
