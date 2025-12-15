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

    @GetMapping
    public String books(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("books", library.search(q));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("newBook", new Book());
        return "books";
    }

    @PostMapping
    public String addBook(@ModelAttribute("newBook") Book b,
                          @RequestParam("coverFile") MultipartFile file) {

        try {
            // ✅ STORE OUTSIDE src/
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);

            String filename = b.getId() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // ✅ PUBLIC URL
            b.setCoverUrl("/uploads/" + filename);

        } catch (Exception e) {
            e.printStackTrace();
        }

        library.addBook(b);
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        library.deleteBook(id);
        return "redirect:/books";
    }
}
