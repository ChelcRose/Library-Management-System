package com.usc.libraryms.model;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    private String id;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String title;
    private String author;
    private String category;
    private String coverUrl;


    private int totalCopies;
    private int availableCopies;

    public Book() {}

    public Book(String id, String title, String author, String category, int totalCopies, String synopsis) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.synopsis = synopsis;
    }


    public String getId() { return id; }
    public String getSynopsis() { return synopsis; }
    public String getCoverUrl() { return coverUrl; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void setId(String id) { this.id = id; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @PrePersist
    public void initAvailableCopies() {
        if (availableCopies == 0) {
            availableCopies = totalCopies;
        }
    }

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
