package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {
    private final LibraryService library;

    public BookController(LibraryService library) { this.library = library; }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("books", library.search(q));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("newBook", new Book());
        return "books";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute("newBook") Book b) {
        library.addBook(b);
        return "redirect:/";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable String id) {
        library.deleteBook(id);
        return "redirect:/";
    }
}
