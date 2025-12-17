package com.usc.libraryms.web;

import com.usc.libraryms.model.Member;
import com.usc.libraryms.model.User;
import com.usc.libraryms.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;


import java.util.Set;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public ProfileController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping
    public String profile(Authentication auth, Model model) {
        User user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("user", user);

        if (user instanceof Member) {
            model.addAttribute("genres", Set.of(
                    "Fiction", "Non-Fiction", "Science", "Technology",
                    "History", "Fantasy", "Romance", "Mystery",
                    "Children's Literature", "Adventure", "Young Adult"
            ));
        }

        return "profile";
    }

    /* ================= CHANGE PASSWORD ================= */

    @PostMapping("/password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 Authentication auth,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {

        User user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "Current password is incorrect");
            return "profile";
        }

        user.setPassword(encoder.encode(newPassword));
        users.save(user);

        new SecurityContextLogoutHandler().logout(request, response, auth);

        return "redirect:/login?passwordChanged";
    }



    /* ================= MEMBER PREFERENCES ================= */

    @PostMapping("/preferences")
    public String updatePreferences(@RequestParam Set<String> categories,
                                    Authentication auth) {

        Member member = (Member) users.findByUsername(auth.getName())
                .orElseThrow();

        member.setPreferredCategories(categories);
        users.save(member);

        return "redirect:/profile?preferencesUpdated";
    }
}
