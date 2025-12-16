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

        /* ================= MEMBER RECOMMENDATIONS ================= */

        if (currentUser instanceof Member member) {

            // 🔹 DEFAULT: show recommended books
            if (filter == null || filter.equals("recommended")) {
                allBooks = library.recommendedFor(member);
            }

            // 🔹 OVERRIDE: show all books
            if ("all".equals(filter)) {
                allBooks = library.search(q);
            }

            // 🔹 OVERRIDE: show available books
            if ("available".equals(filter)) {
                allBooks = library.search(q).stream()
                        .filter(Book::isAvailable)
                        .toList();
            }
        }

        /* ================= NON-MEMBER USERS ================= */

        if (!(currentUser instanceof Member)) {
            // Admin / Librarian logic remains unchanged
            if ("available".equals(filter)) {
                allBooks = allBooks.stream()
                        .filter(Book::isAvailable)
                        .toList();
            }
        }

        /* ================= MODEL ================= */

        model.addAttribute("books", allBooks);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("filter", filter == null ? "recommended" : filter);

        return "dashboard";
    }
}