package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "movie", name = "film_category")
public class FilmCategory {

    @EmbeddedId
    private Id id;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @MapsId("filmId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public FilmCategory() {
    }

    public FilmCategory(Film film, Category category) {
        this.film = film;
        this.category = category;
        this.id = new Id(film.getFilmId(), category.getCategoryId());
    }

    public Id getFilmCategoryId() {
        return id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Embeddable
    public static class Id implements Serializable {

        private Short filmId;

        private Byte categoryId;

        public Id() {
        }

        public Id(Short filmId, Byte categoryId) {
            this.filmId = filmId;
            this.categoryId = categoryId;
        }

        public Short getFilmId() {
            return filmId;
        }

        public void setFilmId(Short filmId) {
            this.filmId = filmId;
        }

        public Byte getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Byte categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id that = (Id) o;
            return Objects.equals(filmId, that.filmId) && Objects.equals(categoryId, that.categoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(filmId, categoryId);
        }
    }
}
