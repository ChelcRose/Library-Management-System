package com.usc.libraryms.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("MEMBER")
public class Member extends User {

    @ElementCollection
    private Set<String> preferredCategories = new HashSet<>();

    private Boolean preferencesSet = false;

    public Member() {}

    public Member(String id, String n, String u, String p) {
        super(id, n, u, p);
        this.preferencesSet = false;
    }

    @Override
    protected Role getRoleInternal() {
        return Role.MEMBER;
    }

    public Set<String> getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(Set<String> preferredCategories) {
        this.preferredCategories = preferredCategories;
        this.preferencesSet = true;
    }

    public boolean isPreferencesSet() {
        return Boolean.TRUE.equals(preferencesSet);
    }
}
