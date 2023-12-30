package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "movie", name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false, updatable = false)
    private Short countryId;

    @Column(name = "country", nullable = false, length = 50)
    private String countryName;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    public Country() {
    }

    public Country(String countryName) {
        this.countryName = countryName;
    }

    public Short getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String country) {
        this.countryName = country;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
