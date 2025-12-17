package com.usc.libraryms.web;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.model.Loan;
import com.usc.libraryms.model.User;
import com.usc.libraryms.repo.BookRepository;
import com.usc.libraryms.repo.UserRepository;
import com.usc.libraryms.service.LibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final LibraryService library;
    private final UserRepository users;
    private final BookRepository books;

    public MemberController(LibraryService library, UserRepository users, BookRepository books) {
        this.library = library;
        this.users = users;
        this.books = books;
    }

    /* ================= MEMBER LOAN HISTORY ================= */

    @GetMapping("/loans")
    public String myLoans(Authentication auth, Model model) {

        User user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        String memberId = user.getUserId();

        List<Loan> activeLoanList = library.activeLoansByMember(memberId);
        List<Map<String, Object>> activeLoansWithBooks = activeLoanList.stream()
                .map(loan -> {
                    Map<String, Object> loanWithBook = new HashMap<>();
                    loanWithBook.put("loan", loan);
                    Book book = books.findById(loan.getBookId()).orElse(null);
                    loanWithBook.put("book", book);
                    return loanWithBook;
                })
                .collect(Collectors.toList());

        List<Loan> allLoans = library.loansByMember(memberId);
        List<Map<String, Object>> returnedLoansWithBooks = allLoans.stream()
                .filter(Loan::isReturned)
                .map(loan -> {
                    Map<String, Object> loanWithBook = new HashMap<>();
                    loanWithBook.put("loan", loan);
                    Book book = books.findById(loan.getBookId()).orElse(null);
                    loanWithBook.put("book", book);
                    return loanWithBook;
                })
                .sorted((a, b) -> {
                    Loan loanA = (Loan) a.get("loan");
                    Loan loanB = (Loan) b.get("loan");

                    if (loanA.getReturnDate() != null && loanB.getReturnDate() != null) {
                        return loanB.getReturnDate().compareTo(loanA.getReturnDate());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        model.addAttribute("activeLoans", activeLoansWithBooks);
        model.addAttribute("returnedLoans", returnedLoansWithBooks);

        return "member-loans";
    }
}
