package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findBankAccountByAccountNumber(String bankAccountNumber);


    @Query(value = "SELECT c.bankAccounts FROM Client c \n" +
            "WHERE c.id=:id")
    List<BankAccount> getBankAccountsByClientsId(@Param("id") Long id);

    @Query(value = "select bankAccount.accountNumber from BankAccount  bankAccount")
    Set<String> getBankAccountNumbers();

    Optional<BankAccount> findBankAccountByIban(String iban);

    @Query(value = "SELECT user_account.name FROM user_account JOIN client_holds_bankaccount ON user_account.id = client_holds_bankaccount.client_id JOIN bank_account ON bank_account.id = client_holds_bankaccount.bank_account_id WHERE bank_account.iban IN (SELECT bank_account.iban FROM bank_account WHERE iban=:iban)", nativeQuery = true)
    List<String> getAllAccountholderNamesByIBAN(String iban);

}

