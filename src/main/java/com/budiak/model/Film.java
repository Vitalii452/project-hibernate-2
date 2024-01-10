package com.budiak.model;

import com.budiak.util.SpecialFeaturesConverter;
import com.budiak.util.YearConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<String> specialFeatures;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public Film() {
    }

    public Film(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.releaseYear = builder.releaseYear;
        this.language = builder.language;
        this.originalLanguage = builder.originalLanguage;
        this.rentalDuration = builder.rentalDuration;
        this.rentalRate = builder.rentalRate;
        this.length = builder.length;
        this.replacementCost = builder.replacementCost;
        this.rating = builder.rating;
        this.specialFeatures = builder.specialFeatures;
    }

    public static class Builder {
        private String title;
        private String description;
        private Integer releaseYear;
        private Language language;
        private Language originalLanguage;
        private Byte rentalDuration = 3;
        private BigDecimal rentalRate = new BigDecimal("4.99");
        private Short length;
        private BigDecimal replacementCost = new BigDecimal("19.99");;
        private Rating rating = Rating.G;
        private Set<String> specialFeatures;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setReleaseYear(Integer releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public Builder setLanguage(Language language) {
            this.language = language;
            return this;
        }

        public Builder setOriginalLanguage(Language originalLanguage) {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public Builder setRentalDuration(Byte rentalDuration) {
            this.rentalDuration = rentalDuration;
            return this;
        }

        public Builder setRentalRate(BigDecimal rentalRate) {
            this.rentalRate = rentalRate;
            return this;
        }

        public Builder setLength(Short length) {
            this.length = length;
            return this;
        }

        public Builder setReplacementCost(BigDecimal replacementCost) {
            this.replacementCost = replacementCost;
            return this;
        }

        public Builder setRating(Rating rating) {
            this.rating = rating;
            return this;
        }

        public Builder setSpecialFeatures(Set<String> specialFeatures) {
            this.specialFeatures = specialFeatures;
            return this;
        }

        public Film build() {
            return new Film(this);
        }
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

    public void setFilmId(Short filmId) {
        this.filmId = filmId;
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

    public Set<String> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(Set<String> specialFeatures) {
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