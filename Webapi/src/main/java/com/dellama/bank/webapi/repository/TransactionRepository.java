package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionById(Long id);

    @Query(value = "SELECT * FROM transaction WHERE toibanbank_account_nr=:bankAccountNr OR fromibanbank_account_nr=:bankAccountNr", nativeQuery = true)
    List<Transaction> findAllTransactionsByBankAccount(@Param("bankAccountNr") String bankAccountNr);

}
