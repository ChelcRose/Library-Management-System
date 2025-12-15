package com.usc.libraryms.config;

import com.usc.libraryms.model.Admin;
import com.usc.libraryms.model.Librarian;
import com.usc.libraryms.model.Member;
import com.usc.libraryms.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public DataLoader(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(new Admin("U1","Admin","admin",encoder.encode("admin")));
            users.save(new Librarian("U2","Librarian","librarian",encoder.encode("librarian")));
            users.save(new Member("U3","Student","student",encoder.encode("student")));
        }
    }
}
