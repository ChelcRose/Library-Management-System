package com.usc.libraryms.web;

import com.usc.libraryms.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                         @RequestParam int days) {
        library.borrow(bookId, memberId, days);
        return "redirect:/loans";
    }

    @PostMapping("/return")
    public String returnLoan(@RequestParam String loanId) {
        double fine = library.returnLoan(loanId);
        return "redirect:/loans?fine=" + fine;
    }
}
