package com.usc.libraryms.model;

import jakarta.persistence.Entity;

@Entity
public class Member extends User {
    private double outstandingFine;

    public Member() {}
    public Member(String id, String name, String username, String pw) { super(id, name, username, pw); }

    @Override public Role getRole() { return Role.MEMBER; }

    public double getOutstandingFine() { return outstandingFine; }
    public void addFine(double amount) { outstandingFine += Math.max(0, amount); }
    public void payFine(double amount) { outstandingFine = Math.max(0, outstandingFine - Math.max(0, amount)); }
}
