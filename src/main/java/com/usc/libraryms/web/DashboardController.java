package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.model.Member;
import com.usc.libraryms.model.User;
import com.usc.libraryms.repo.UserRepository;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    private final LibraryService library;
    private final UserRepository users;

    // ✅ FIX: inject UserRepository
    public DashboardController(LibraryService library, UserRepository users) {
        this.library = library;
        this.users = users;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String filter,
            Authentication auth,
            Model model) {

        User currentUser = null;

        if (auth != null && auth.isAuthenticated()) {
            currentUser = users.findByUsername(auth.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            // ✅ FIRST-TIME MEMBER REDIRECT (UNCHANGED)
            if (currentUser instanceof Member m && !m.isPreferencesSet()) {
                return "redirect:/member/preferences";
            }
        }

        /* ================= BASE BOOK LIST ================= */

        var allBooks = library.search(q);

        String sectionTitle = "All Books"; // ✅ NEW
        List<Book> recommended = List.of(); // ✅ NEW

        /* ================= MEMBER LOGIC ================= */

        if (currentUser instanceof Member member) {

            recommended = library.recommendedFor(member); // ✅ NEW

            if (filter == null || filter.equals("recommended")) {
                allBooks = recommended;
                sectionTitle = "Recommended Books";
            }

            if ("all".equals(filter)) {
                allBooks = library.search(q);
                sectionTitle = "All Books";
            }

            if ("available".equals(filter)) {
                allBooks = library.search(q).stream()
                        .filter(Book::isAvailable)
                        .toList();
                sectionTitle = "Available Books";
            }
        }

        /* ================= NON-MEMBER USERS (UNCHANGED LOGIC) ================= */

        if (!(currentUser instanceof Member)) {
            if ("available".equals(filter)) {
                allBooks = allBooks.stream()
                        .filter(Book::isAvailable)
                        .toList();
                sectionTitle = "Available Books";
            }
        }

        /* ================= MODEL ATTRIBUTES ================= */

        model.addAttribute("books", allBooks);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("filter", filter == null ? "recommended" : filter);

        // ✅ NEW (for cards + title)
        model.addAttribute("sectionTitle", sectionTitle);
        model.addAttribute("allBooks", library.allBooks().size());
        model.addAttribute("recommendedBooks", recommended.size());
        model.addAttribute("borrowedBooks",
                library.allBooks().stream().filter(b -> !b.isAvailable()).count());
        model.addAttribute("activeMembers",
                users.findAll().stream().filter(u -> u instanceof Member).count());

        return "dashboard";
    }
}