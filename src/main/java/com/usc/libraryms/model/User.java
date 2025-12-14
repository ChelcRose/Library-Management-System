package com.usc.libraryms.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    protected String userId;

    protected String name;

    @Column(unique = true, nullable = false)
    protected String username;

    protected String passwordHash;

    protected User() {}

    protected User(String userId, String name, String username, String passwordHash) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getUsername() { return username; }

    public void setName(String name) { this.name = name; }
    public boolean verifyPassword(String raw) { return passwordHash != null && passwordHash.equals(raw); }

    public abstract Role getRole(); // polymorphism hook
}
