package com.usc.libraryms.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    public Admin() {}
    public Admin(String id, String n, String u, String p) { super(id,n,u,p); }
    @Override public Role getRole() { return Role.ADMIN; }
}
