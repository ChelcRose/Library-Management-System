package com.usc.libraryms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Book {

    @Id
    private String id;

    private String title;
    private String author;
    private String category;

    private int totalCopies;
    private int availableCopies;

    public Book() {}

    public Book(String id, String title, String author, String category, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    /* ========= GETTERS ========= */
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    /* ========= SETTERS (REQUIRED FOR FORMS) ========= */
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    /* ========= SAFETY INIT ========= */
    @PrePersist
    public void initAvailableCopies() {
        if (availableCopies == 0) {
            availableCopies = totalCopies;
        }
    }

    /* ========= DOMAIN LOGIC ========= */
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void borrowOne() {
        if (!isAvailable()) throw new IllegalStateException("No copies available");
        availableCopies--;
    }

    public void returnOne() {
        if (availableCopies >= totalCopies) throw new IllegalStateException("All copies already returned");
        availableCopies++;
    }
}
