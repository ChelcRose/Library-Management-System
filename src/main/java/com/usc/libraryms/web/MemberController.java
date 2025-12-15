package com.usc.libraryms.web;

import com.usc.libraryms.model.User;
import com.usc.libraryms.repo.UserRepository;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final LibraryService library;
    private final UserRepository users;

    public MemberController(LibraryService library, UserRepository users) {
        this.library = library;
        this.users = users;
    }

    /* ================= MEMBER LOAN HISTORY ================= */

    @GetMapping("/loans")
    public String myLoans(Authentication auth, Model model) {

        User user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("activeLoans",
                library.activeLoansByMember(user.getUserId()));

        model.addAttribute("allLoans",
                library.loansByMember(user.getUserId()));

        return "member-loans";
    }
}
