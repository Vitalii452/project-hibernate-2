package com.budiak.model;

import com.budiak.util.SpecialFeaturesConverter;
import com.budiak.util.YearConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(schema = "movie", name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", nullable = false, updatable = false)
    private Short filmId;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_year")
    @Convert(converter = YearConverter.class)
    private Integer releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration", nullable = false)
    private Byte rentalDuration = 3;

    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rentalRate;

    @Column(name = "length")
    private Short length;

    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    private BigDecimal replacementCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", columnDefinition = "ENUM('G', 'PG', 'PG_13', 'R', 'NC_17')")
    private Rating rating = Rating.G;

    @Convert(converter = SpecialFeaturesConverter.class)
    @Column(name = "special_features")
    private String specialFeatures;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public Film() {
    }

    public Film(String title, String description, Integer releaseYear, Language language, Language originalLanguage, Byte rentalDuration, BigDecimal rentalRate, Short length, BigDecimal replacementCost, Rating rating, String specialFeatures) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
    }

    public BigDecimal getRentalRate() {
        if (rentalRate == null) {
            rentalRate = new BigDecimal("4.99");
        }
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public BigDecimal getReplacementCost() {
        if (replacementCost == null) {
            replacementCost = new BigDecimal("19.99");
        }
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public Short getFilmId() {
        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Byte getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(Byte rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public enum Rating {
        G, PG, PG_13, R, NC_17
    }
}