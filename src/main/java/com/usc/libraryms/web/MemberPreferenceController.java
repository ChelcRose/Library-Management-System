package com.usc.libraryms.web;

import com.usc.libraryms.model.Member;
import com.usc.libraryms.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/member/preferences")
public class MemberPreferenceController {

    private final UserRepository users;

    public MemberPreferenceController(UserRepository users) {
        this.users = users;
    }

    @GetMapping
    public String preferencesPage(Model model) {
        model.addAttribute("categories", Set.of(
                "Fiction", "Non-Fiction", "Science", "Technology",
                "History", "Fantasy", "Romance", "Mystery"
        ));
        return "member-preferences";
    }

    @PostMapping
    public String savePreferences(@RequestParam Set<String> categories,
                                  Authentication auth) {

        Member member = (Member) users.findByUsername(auth.getName())
                .orElseThrow();

        member.setPreferredCategories(categories);
        users.save(member);

        return "redirect:/dashboard";
    }
}
