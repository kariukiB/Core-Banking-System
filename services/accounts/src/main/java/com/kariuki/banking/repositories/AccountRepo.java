package com.kariuki.banking.repositories;

import com.kariuki.banking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    boolean existsByCustomerId(String customerId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
