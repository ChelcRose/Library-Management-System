package com.usc.libraryms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public boolean isAvailable() { return availableCopies > 0; }

    public void borrowOne() {
        if (!isAvailable()) throw new IllegalStateException("No copies available");
        availableCopies--;
    }

    public void returnOne() {
        if (availableCopies >= totalCopies) throw new IllegalStateException("All copies already returned");
        availableCopies++;
    }
}
