package com.usc.libraryms.repo;

import com.usc.libraryms.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {

    // loans not yet returned (admin / librarian use)
    List<Loan> findByReturnDateIsNull();

    // ✅ NEW: all loans by a specific member (history + active)
    List<Loan> findByMemberId(String memberId);
}
