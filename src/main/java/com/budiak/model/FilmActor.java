package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "movie", name = "film_actor")
public class FilmActor {

    @EmbeddedId
    private FilmActorId filmActorId;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @MapsId("actorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @MapsId("filmId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    public FilmActor() {
    }

    public FilmActor(Actor actor, Film film) {
        this.actor = actor;
        this.film = film;
        this.filmActorId = new FilmActorId(actor.getActorId(), film.getFilmId());
    }

    public FilmActorId getFilmActorId() {
        return filmActorId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Embeddable
    public static class FilmActorId implements Serializable {

        private Long actorId;

        private Long filmId;


        public FilmActorId() {
        }

        public FilmActorId(Long actorId, Long filmId) {
            this.actorId = actorId;
            this.filmId = filmId;
        }

        public Long getActorId() {
            return actorId;
        }

        public void setActorId(Long actorId) {
            this.actorId = actorId;
        }

        public Long getFilmId() {
            return filmId;
        }

        public void setFilmId(Long filmId) {
            this.filmId = filmId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilmActorId that = (FilmActorId) o;
            return Objects.equals(actorId, that.actorId) && Objects.equals(filmId, that.filmId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(actorId, filmId);
        }
    }
}
