package com.dellama.bank.webapi.model.service;

import com.dellama.bank.webapi.DTO.TransactionDTO;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.Transaction;
import com.dellama.bank.webapi.model.TransactionResult;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
    }

    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findTransactionById(id);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findAllTransactionsByBankAccount(String bankAccountNr) {
        return transactionRepository.findAllTransactionsByBankAccount(bankAccountNr);
    }

    public TransactionResult save(TransactionDTO transactionDTO) {
        Optional<BankAccount> fromAccount = Optional.empty();
        Optional<BankAccount> toAccount = Optional.empty();

        if (transactionDTO.getAmount() == null) {
            return TransactionResult.AMOUNT_ERROR;
        }

        // check if the IBAN account number is a valid internal IBAN. If so, retrieve the bankAccount.
        if (IbanUtils.isValidInternalIban(transactionDTO.getFromIBANBankAccountNr())) {
            fromAccount = bankAccountService.findBankAccountByIban(transactionDTO.getFromIBANBankAccountNr());
        }
//        todo here you want an else statement for api call to other bank when its not a LlamaBank account

        if (IbanUtils.isValidInternalIban(transactionDTO.getToIBANBankAccountNr())) {
            toAccount = bankAccountService.findBankAccountByIban(transactionDTO.getToIBANBankAccountNr());
        }
//        todo here you want an else statement for api call to other bank when its not a LlamaBank account

        if (fromAccount.isEmpty() || toAccount.isEmpty()) {
            return TransactionResult.ACCOUNT_ERROR;
        }
        if (fromAccount.get().getBalance() < transactionDTO.getAmount()) {
            return TransactionResult.BALANCE_ERROR;
        }
        if (fromAccount.get().equals(toAccount.get())) {
            return TransactionResult.SAME_ACCOUNT_ERROR;
        }
        if (transactionDTO.getAmount() <= 0) {
            return TransactionResult.AMOUNT_ERROR;
        }

        Transaction transaction = new Transaction(transactionDTO.getFromIBANBankAccountNr(), transactionDTO.getToIBANBankAccountNr(), transactionDTO.getAmount(), transactionDTO.getMessage(), LocalDateTime.now());

        // after transaction is added to the transaction list, then start process Transaction.
        transactionRepository.save(transaction);
        processTransaction(transaction, fromAccount.get(), toAccount.get());
        return TransactionResult.TRANSACTION_OK;
    }

    private void processTransaction(Transaction transaction, BankAccount fromAccount, BankAccount toAccount) {
        double amount = transaction.getAmount();

        // set balance for the sender and retriever of the money
        fromAccount.setBalance((fromAccount.getBalance()) - amount);
        toAccount.setBalance((toAccount.getBalance()) + amount);

        // save it in the repository
        bankAccountService.save(fromAccount);
        bankAccountService.save(toAccount);
    }

}
