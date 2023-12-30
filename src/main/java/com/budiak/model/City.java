package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "movie", name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false, updatable = false)
    private Short cityId;

    @Column(name = "city", nullable = false, length = 50)
    private String cityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public City() {
    }

    public City(String cityName, Country country) {
        this.cityName = cityName;
        this.country = country;
    }

    public Short getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String city) {
        this.cityName = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
