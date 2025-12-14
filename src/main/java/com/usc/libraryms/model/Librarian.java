package com.usc.libraryms.model;

import jakarta.persistence.Entity;

@Entity
public class Librarian extends User {
    public Librarian() {}
    public Librarian(String id, String name, String username, String pw) { super(id, name, username, pw); }
    @Override public Role getRole() { return Role.LIBRARIAN; }
}
