package com.kariuki.banking.repository;

import com.kariuki.banking.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {
}
