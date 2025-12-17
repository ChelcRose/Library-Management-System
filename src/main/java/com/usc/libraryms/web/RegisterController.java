package com.usc.libraryms.web;

import com.usc.libraryms.model.Member;
import com.usc.libraryms.repo.UserRepository;
import com.usc.libraryms.web.dto.RegisterForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public RegisterController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }


    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping
    public String registerMember(@ModelAttribute RegisterForm form, Model model) {


        if (users.findByUsername(form.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }


        String id = generateNextUserId();

        Member member = new Member(
                id,
                form.getName(),
                form.getUsername(),
                encoder.encode(form.getPassword())
        );

        users.save(member);
        return "redirect:/login?registered";
    }


    private String generateNextUserId() {
        int nextId = users.findAllUserIds().stream()
                .filter(id -> id.matches("U\\d+")) // ✅ ONLY U1, U2, U3...
                .map(id -> id.substring(1))        // remove "U"
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        return "U" + nextId;
    }

}
