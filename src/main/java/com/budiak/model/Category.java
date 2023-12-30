package com.budiak.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "movie", name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, updatable = false)
    private Byte categoryId;

    @Column(name = "name", length = 25, nullable = false)
    private String categoryName;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Byte getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
