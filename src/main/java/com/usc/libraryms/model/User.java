package com.usc.libraryms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
public abstract class User {

    @Id
    @Column(name = "user_id")
    protected String userId;

    protected String name;

    @Column(unique = true)
    protected String username;

    protected String password;

    protected User() {}

    protected User(String id, String name, String username, String password) {
        this.userId = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract Role getRole();
}
