package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.TransactionDTO;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.Transaction;
import com.dellama.bank.webapi.model.TransactionResult;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.model.service.BankAccountService;
import com.dellama.bank.webapi.model.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;

    public TransactionController(TransactionService transactionService, BankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @CrossOrigin
    @GetMapping()
    public List<Transaction> getAllTransactions() {
        return transactionService.findAll();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transaction>> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> foundTransaction = transactionService.findTransactionById(id);

        if (foundTransaction.isPresent()) {
            return new ResponseEntity<>(foundTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * @param bankAccountNr the bankaccountnumber of a bankaccount
     * @return list of transactions made/received by the given bankaccountnumber
     * @should return a list of all transactions made/received by the given bankaccountnumber
     */
    @CrossOrigin
    @GetMapping("/bankAccountNr/{bankAccountNr}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByBankAccountNr(@PathVariable String bankAccountNr) {
        //check if it's a bankaccountnr without iban, if so transform it into a IBAN number.
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNr);
        if (bankAccount.isPresent()) {
            bankAccountNr = IbanUtils.internalBankAccountNumberToIban(bankAccountNr);
        }

        List<Transaction> transactions = transactionService.findAllTransactionsByBankAccount(bankAccountNr);

        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @GetMapping("/bankAccountByAccountNr/{bankAccountNr}")
    public ResponseEntity<Optional<BankAccount>> getBankAccountByBankAccountNr(@PathVariable String bankAccountNr) {
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNr);

        if (bankAccount.isPresent()) {
            return new ResponseEntity<>(bankAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * @param transactionDTO Transaction
     * @return a responseEntity which shows if transaction is succesful.
     */
    @PostMapping(value = "/add")
    public ResponseEntity<String> addTransaction(@RequestBody TransactionDTO transactionDTO) {

        TransactionResult result = transactionService.save(transactionDTO);

        if (result != TransactionResult.TRANSACTION_OK) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result.getTransactionStatus());
        } else return ResponseEntity.ok(result.getTransactionStatus());
    }
}
