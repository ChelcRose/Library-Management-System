package com.usc.libraryms.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {}
    public Admin(String id, String name, String username, String pw) { super(id, name, username, pw); }
    @Override public Role getRole() { return Role.ADMIN; }
}
