package com.bemojr.bankAccountManagement.repository;

import com.bemojr.bankAccountManagement.entity.BankAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, Long> {
    Optional<BankAccountDetails> findByAccountNumber(String accountNumber);
}
