package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final LibraryService library;

    public DashboardController(LibraryService library) {
        this.library = library;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        model.addAttribute("username", auth.getName());

        // ✅ books for all roles
        model.addAttribute("books", library.allBooks());

        // ✅ needed ONLY if admin/librarian sees add form
        model.addAttribute("newBook", new Book());

        return "dashboard";
    }

}
