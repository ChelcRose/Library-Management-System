package com.usc.libraryms.service;

import com.usc.libraryms.model.Book;
import com.usc.libraryms.model.Loan;
import com.usc.libraryms.repo.BookRepository;
import com.usc.libraryms.repo.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {
    private final BookRepository books;
    private final LoanRepository loans;
    private final FinePolicy finePolicy;

    public LibraryService(BookRepository books, LoanRepository loans, FinePolicy finePolicy) {
        this.books = books;
        this.loans = loans;
        this.finePolicy = finePolicy;
    }

    public List<Book> allBooks() { return books.findAll(); }

    public List<Book> search(String q) {
        if (q == null || q.isBlank()) return books.findAll();
        return books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(q, q, q);
    }

    public void addBook(Book b) { books.save(b); }
    public void deleteBook(String id) { books.deleteById(id); }

    public List<Loan> activeLoans() { return loans.findByReturnDateIsNull(); }
    public List<Loan> allLoans() { return loans.findAll(); }

    @Transactional
    public Loan borrow(String bookId, String memberId, int days) {
        Book book = books.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.borrowOne();
        books.save(book);

        String loanId = "LN-" + (loans.count() + 1);
        Loan loan = new Loan(loanId, bookId, memberId, LocalDate.now(), LocalDate.now().plusDays(days));
        return loans.save(loan);
    }

    @Transactional
    public double returnLoan(String loanId) {
        Loan loan = loans.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        if (loan.isReturned()) throw new IllegalStateException("Already returned");

        Book book = books.findById(loan.getBookId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.returnOne();
        books.save(book);

        LocalDate returned = LocalDate.now();
        loan.markReturned(returned);
        loans.save(loan);

        return finePolicy.computeFine(loan.getDueDate(), returned);
    }
}
