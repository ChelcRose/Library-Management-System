package com.usc.libraryms.web;

import com.usc.libraryms.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LibraryService library;

    public LoanController(LibraryService library) {
        this.library = library;
    }

    @GetMapping
    public String loans(Model model) {
        model.addAttribute("loans", library.allLoans());
        return "loans";
    }

    @PostMapping("/borrow")
    public String borrow(@RequestParam String bookId,
                         @RequestParam String memberId,
                         @RequestParam int days,
                         RedirectAttributes redirectAttributes) {
        try {
            library.borrow(bookId, memberId, days);
            redirectAttributes.addFlashAttribute("success", "Book borrowed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Book not found: " + e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No copies available");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to borrow book");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/return")
    public String returnLoan(@RequestParam String loanId,
                            RedirectAttributes redirectAttributes) {
        try {
            double fine = library.returnLoan(loanId);
            if (fine > 0) {
                redirectAttributes.addFlashAttribute("fine", "Fine: $" + String.format("%.2f", fine));
            } else {
                redirectAttributes.addFlashAttribute("success", "Book returned successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to return book: " + e.getMessage());
        }
        return "redirect:/loans";
    }
}
