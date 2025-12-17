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
import java.util.Collections;

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
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
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

            if ("unavailable".equals(filter)) {
                allBooks = library.search(q).stream()
                        .filter(b -> !b.isAvailable())
                        .toList();
                sectionTitle = "Unavailable Books";
            }

        }

        if (genre != null && !genre.isBlank()) {
            allBooks = allBooks.stream()
                    .filter(b -> genre.equalsIgnoreCase(b.getCategory()))
                    .toList();

            sectionTitle = genre + " Books";
        }

        int pageSize = 12;
        int totalBooks = allBooks.size();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalBooks);

        if (fromIndex > totalBooks) {
            allBooks = Collections.emptyList();
        } else {
            allBooks = allBooks.subList(fromIndex, toIndex);
        }


        /* ================= MODEL ATTRIBUTES ================= */
        model.addAttribute("availableBooks",
                library.allBooks().stream().filter(Book::isAvailable).count());
        model.addAttribute("genres", List.of(
                "Fiction", "Non-Fiction", "Science", "Technology",
                "History", "Fantasy", "Romance", "Mystery",
                "Children's Literature", "Adventure", "Young Adult"
        ));

        model.addAttribute("books", allBooks);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("filter", filter == null ? "recommended" : filter);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sectionTitle", sectionTitle);
        model.addAttribute("selectedGenre", genre == null ? "" : genre);
        model.addAttribute("allBooks", library.allBooks().size());
        model.addAttribute("recommendedBooks", recommended.size());
        model.addAttribute("borrowedBooks",
                library.allBooks().stream().filter(b -> !b.isAvailable()).count());
        model.addAttribute("activeMembers",
                users.findAll().stream().filter(u -> u instanceof Member).count());

        return "dashboard";
    }

}