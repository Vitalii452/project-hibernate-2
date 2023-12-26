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
    private FilmCategoryId filmCategoryId;

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
        this.filmCategoryId = new FilmCategoryId(film.getFilmId(), category.getCategoryId());
    }

    public FilmCategoryId getFilmCategoryId() {
        return filmCategoryId;
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
    public static class FilmCategoryId implements Serializable {

        private Long filmId;

        private Long categoryId;

        public FilmCategoryId() {
        }

        public FilmCategoryId(Long filmId, Long categoryId) {
            this.filmId = filmId;
            this.categoryId = categoryId;
        }

        public Long getFilmId() {
            return filmId;
        }

        public void setFilmId(Long filmId) {
            this.filmId = filmId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilmCategoryId that = (FilmCategoryId) o;
            return Objects.equals(filmId, that.filmId) && Objects.equals(categoryId, that.categoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(filmId, categoryId);
        }
    }
}
