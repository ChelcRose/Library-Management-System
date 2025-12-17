package com.usc.libraryms.repo;

import com.usc.libraryms.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {

    List<Loan> findByReturnDateIsNull();

    List<Loan> findByMemberId(String memberId);
}
