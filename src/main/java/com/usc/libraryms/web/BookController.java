package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final LibraryService library;

    public BookController(LibraryService library) {
        this.library = library;
    }

    /* ================= VIEW BOOKS ================= */

    @GetMapping
    public String books(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("books", library.search(q));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("newBook", new Book());
        return "books";
    }

    /* ================= ADD BOOK ================= */

    @PostMapping
    public String addBook(@ModelAttribute("newBook") Book b,
                          @RequestParam("coverFile") MultipartFile file) {

        // auto-generate book ID
        b.setId(library.generateNextBookId());

        try {
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);

            String filename = b.getId() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            b.setCoverUrl("/uploads/" + filename);

        } catch (Exception e) {
            e.printStackTrace();
        }

        library.addBook(b);
        return "redirect:/books";
    }

    /* ================= UPDATE BOOK (MODAL) ================= */

    @PostMapping("/update")
    public String updateBook(@RequestParam String id,
                             @RequestParam String title,
                             @RequestParam String author,
                             @RequestParam String category,
                             @RequestParam int totalCopies) {

        Book book = library.allBooks().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow();

        // preserve borrowed copies
        int borrowed = book.getTotalCopies() - book.getAvailableCopies();

        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(Math.max(0, totalCopies - borrowed));

        library.addBook(book); // save/update
        return "redirect:/books";
    }

    /* ================= DELETE BOOK ================= */

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        library.deleteBook(id);
        return "redirect:/books";
    }
}
