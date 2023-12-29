package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "movie", name = "actor")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id", nullable = false, updatable = false)
    private Short actorId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "actor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FilmActor> filmActor;

    public Actor() {
        this.filmActor = new HashSet<>();
    }

    public Actor(String firstName, String lastName, Set<FilmActor> filmActor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.filmActor = filmActor;
    }

    public Short getActorId() {
        return actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Set<FilmActor> getFilmActor() {
        return filmActor;
    }

    public void setFilmActor(Set<FilmActor> filmActor) {
        this.filmActor = filmActor;
    }
}
