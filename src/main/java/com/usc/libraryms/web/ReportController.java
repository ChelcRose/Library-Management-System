package com.usc.libraryms.web;

import org.springframework.ui.Model;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final LibraryService library;

    public ReportController(LibraryService library) {
        this.library = library;
    }

    @GetMapping
    public String reports(@RequestParam(required = false) String type,
                          Model model) {

        if (type == null) type = "borrowed";

        model.addAttribute("type", type);
        model.addAttribute("active", "reports");
        model.addAttribute("books", java.util.Collections.emptyList());
        model.addAttribute("loans", java.util.Collections.emptyList());

        if ("available".equals(type)) {
            model.addAttribute(
                    "books",
                    library.allBooks().stream()
                            .filter(Book::isAvailable)
                            .toList()
            );
        }
        else if ("unavailable".equals(type)) {
            model.addAttribute(
                    "books",
                    library.allBooks().stream()
                            .filter(b -> !b.isAvailable())
                            .toList()
            );
        }
        else {
            model.addAttribute(
                    "loans",
                    library.activeLoans()
            );
        }

        return "reports";
    }


}
