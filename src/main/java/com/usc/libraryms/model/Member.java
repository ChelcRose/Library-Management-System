package com.usc.libraryms.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MEMBER")
public class Member extends User {
    public Member() {}
    public Member(String id, String n, String u, String p) { super(id,n,u,p); }
    @Override protected Role getRoleInternal() { return Role.MEMBER; }
}
