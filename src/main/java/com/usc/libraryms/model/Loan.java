package com.usc.libraryms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    private String loanId;

    private String bookId;
    private String memberId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan() {}

    public Loan(String loanId, String bookId, String memberId, LocalDate borrowDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getLoanId() { return loanId; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public boolean isReturned() { return returnDate != null; }
    public void markReturned(LocalDate date) { this.returnDate = date; }
}
