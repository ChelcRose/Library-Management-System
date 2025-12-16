package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final LibraryService library;

    public DashboardController(LibraryService library) {
        this.library = library;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String filter,
            Authentication auth,
            Model model) {

        var allBooks = library.search(q);

        if ("available".equals(filter)) {
            allBooks = allBooks.stream()
                    .filter(Book::isAvailable)
                    .toList();
        }

        model.addAttribute("books", allBooks);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("filter", filter == null ? "all" : filter);

        return "dashboard";
    }


}
