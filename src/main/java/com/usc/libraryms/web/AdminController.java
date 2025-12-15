package com.usc.libraryms.web;

import com.usc.libraryms.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository users;

    public AdminController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", users.findAll());
        return "admin-users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable String id) {
        users.deleteById(id);
        return "redirect:/admin/users";
    }
}
