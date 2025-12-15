package com.usc.libraryms.web;

import com.usc.libraryms.model.*;
import com.usc.libraryms.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AdminController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    /* ================= VIEW USERS ================= */

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", users.findAll());
        return "admin-users";
    }

    /* ================= DELETE USER ================= */

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable String id) {
        users.deleteById(id);
        return "redirect:/admin/users";
    }

    /* ================= CREATE USER ================= */

    @PostMapping("/users/create")
    public String createUser(@RequestParam String name,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam Role role) {

        // ✅ FIXED: use sequential ID
        String id = generateNextUserId();
        String encoded = encoder.encode(password);

        User user;

        switch (role) {
            case ADMIN -> user = new Admin(id, name, username, encoded);
            case LIBRARIAN -> user = new Librarian(id, name, username, encoded);
            default -> user = new Member(id, name, username, encoded);
        }

        users.save(user);
        return "redirect:/admin/users";
    }

    /* ================= HELPER METHOD ================= */
    // 📌 MUST be INSIDE the class, but OUTSIDE other methods

    private String generateNextUserId() {
        int nextId = users.findAllUserIds().stream()
                .filter(id -> id.matches("U\\d+")) // only U1, U2, ...
                .map(id -> id.substring(1))        // remove "U"
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        return "U" + nextId;
    }
}
