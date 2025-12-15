package com.usc.libraryms.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LIBRARIAN")
public class Librarian extends User {
    public Librarian() {}
    public Librarian(String id, String n, String u, String p) { super(id,n,u,p); }
    @Override public Role getRole() { return Role.LIBRARIAN; }
}
