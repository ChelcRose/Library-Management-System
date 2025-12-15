package com.usc.libraryms.web;

import com.usc.libraryms.model.User;
import com.usc.libraryms.repo.UserRepository;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LibraryService library;
    private final UserRepository users;

    public LoanController(LibraryService library, UserRepository users) {
        this.library = library;
        this.users = users;
    }

    /* ================= VIEW LOANS (ADMIN / LIBRARIAN) ================= */

    @GetMapping
    public String loans(Model model) {
        model.addAttribute("loans", library.allLoans());
        return "loans";
    }

    /* ================= BORROW ================= */

    @PostMapping("/borrow")
    public String borrow(@RequestParam String bookId,
                         @RequestParam int days,
                         Authentication auth,
                         RedirectAttributes redirectAttributes) {

        try {
            // ✅ get logged-in user
            String username = auth.getName();
            String memberId = users.findByUsername(username)
                    .orElseThrow()
                    .getUserId();

            library.borrow(bookId, memberId, days);
            redirectAttributes.addFlashAttribute("success", "Book borrowed successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }


    /* ================= RETURN ================= */

    @PostMapping("/return")
    public String returnLoan(@RequestParam String loanId,
                             RedirectAttributes ra) {
        try {
            double fine = library.returnLoan(loanId);

            if (fine > 0) {
                ra.addFlashAttribute("fine", "Fine: ₱" + fine);
            } else {
                ra.addFlashAttribute("success", "Book returned successfully!");
            }

        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/loans";
    }
}
