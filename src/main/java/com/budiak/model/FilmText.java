package com.budiak.model;

import jakarta.persistence.*;

@Entity
@Table(schema = "movie", name = "film_text")
public class FilmText {

    @Id
    private Short filmId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String text;

    public FilmText() {
    }

    public FilmText(Film film, String title, String text) {
        this.film = film;
        this.title = title;
        this.text = text;
    }

    public Short getFilmId() {
        return filmId;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
