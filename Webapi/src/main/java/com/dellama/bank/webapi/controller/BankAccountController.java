package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.BankAccountDTO;
import com.dellama.bank.webapi.model.AccountManager;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.PrivateBankAccount;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {
    @Autowired
    BankAccountRepository bankAccountRepository;
    ClientRepository clientRepository;
    SmallBusinessBankAccountRepository smallBusinessBankAccountRepository;
    PrivateBankAccountRepository privateBankAccountRepository;
    AccountManagerRepository accountManagerRepository;


    public BankAccountController(BankAccountRepository bankAccountRepository, ClientRepository clientRepository, SmallBusinessBankAccountRepository smallBusinessBankAccountRepository, PrivateBankAccountRepository privateBankAccountRepository, AccountManagerRepository accountManagerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
        this.smallBusinessBankAccountRepository = smallBusinessBankAccountRepository;
        this.privateBankAccountRepository = privateBankAccountRepository;
        this.accountManagerRepository = accountManagerRepository;
    }

    /**
     * generic method to cast classes using mapper class
     *
     * @param source      source class
     * @param targetClass target class
     * @param <S>         source class
     * @param <T>         target class
     * @return list of target class objects
     */
    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }


    /**
     * @param id account manager id
     * @return list of 10 bank accounts sorted by balance
     */
    @GetMapping(value = "/bankAccountsByBalance")
    public ResponseEntity<List<BankAccountDTO>> bankAccountsWithHighestBalance(@RequestParam Long id) {
        Pageable pageWithTenBankAccounts = PageRequest.of(0, 10, Sort.by("balance").descending());

//check is a.m. is small business manager
        Optional<AccountManager> accountManagerFromDatabase = accountManagerRepository.findById(id);
        if (accountManagerFromDatabase.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<SmallBusinessBankAccount> smallBusinessAccounts;
        List<PrivateBankAccount> privateBankAccounts;
        if ( Boolean.TRUE.equals(accountManagerFromDatabase.get().isSmallBusinessManager()) ) {
            //return list of small business accounts as  DTO pageable 10 st
            smallBusinessAccounts = smallBusinessBankAccountRepository.findAll(pageWithTenBankAccounts).stream().toList();
            return getBankAccountsListResponseEntity(smallBusinessAccounts);
        } else {
            //return list of private business accounts as  DTO pageable 10 st
            privateBankAccounts = privateBankAccountRepository.findAll(pageWithTenBankAccounts).stream().toList();
            return getBankAccountsListResponseEntity(privateBankAccounts);
        }
    }

    /**
     * generic method to create a response entity
     *
     * @param bankAccountsList list of bank accounts
     * @param <T>              small business or private bank account class
     * @return response entity( list of bank accounts or no content
     */
    <T> ResponseEntity<List<BankAccountDTO>> getBankAccountsListResponseEntity(List<T> bankAccountsList) {
        if ( !bankAccountsList.isEmpty() ) {
            List<BankAccountDTO> bankAccountsDTOList = mapList(bankAccountsList, BankAccountDTO.class);
            return new ResponseEntity<>(bankAccountsDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> bankAccountsByClient(@RequestParam Long id) {
        List<BankAccount> bankAccountsList = bankAccountRepository.getBankAccountsByClientsId(id);
        List<BankAccountDTO> bankAccountDTOList = mapList(bankAccountsList, BankAccountDTO.class);
        return new ResponseEntity<>(bankAccountDTOList, HttpStatus.OK);
    }

    @GetMapping("/accountholders/{iban}")
    public ResponseEntity<List<String>> getAllAccountholderNamesByIBAN(@PathVariable String iban) {

        //check if it's a bankaccountnr without iban, if so transform it into a IBAN number.
        Optional<BankAccount> bankAccount = bankAccountRepository.findBankAccountByAccountNumber(iban);
        if (bankAccount.isPresent()) {
            iban = IbanUtils.internalBankAccountNumberToIban(iban);
        }

        List<String> accountHolderNames = bankAccountRepository.getAllAccountholderNamesByIBAN(iban);
        return new ResponseEntity<>(accountHolderNames, HttpStatus.OK);
    }
}





















