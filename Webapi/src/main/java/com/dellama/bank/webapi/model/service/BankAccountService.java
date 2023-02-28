package com.dellama.bank.webapi.model.service;

import com.dellama.bank.webapi.model.*;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.repository.BankAccountRepository;
import com.dellama.bank.webapi.repository.PinMachineRepository;
import com.dellama.bank.webapi.repository.SmallBusinessBankAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final PinMachineRepository pinMachineRepository;
    private final SmallBusinessBankAccountRepository smallBusinessBankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, PinMachineRepository pinMachineRepository, SmallBusinessBankAccountRepository smallBusinessBankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.pinMachineRepository = pinMachineRepository;
        this.smallBusinessBankAccountRepository = smallBusinessBankAccountRepository;
    }

    public Optional<BankAccount> findById(Long id) {
        return bankAccountRepository.findById(id);
    }

    public Optional<BankAccount> findBankAccountByAccountNumber(String accountNumber) {
        return bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
    }

    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public PrivateBankAccount createPrivateBankAccount(Client client) {
        String accountNumber = getUniqueBankAccountNumber("private");
        var bankAccount = new PrivateBankAccount(accountNumber, client);
        bankAccount.setIban(IbanUtils.internalBankAccountNumberToIban(accountNumber));
        return bankAccountRepository.save(bankAccount);
    }

    public SmallBusinessBankAccount createSmallBusinessBankAccount(Client client, Sector sector) {
        String accountNumber = getUniqueBankAccountNumber("smallBusinessAccount");
        var bankAccount = new SmallBusinessBankAccount(accountNumber, client, sector);
        bankAccount.setIban(IbanUtils.internalBankAccountNumberToIban(accountNumber));
        return bankAccountRepository.save(bankAccount);
    }

    private String getUniqueBankAccountNumber(String accountType) {
        var bankAccountNumbersInDatabase = bankAccountRepository.getBankAccountNumbers();
        String accountNumber;
        do {
            accountNumber = RandomBankAccountNumberGenerator.generateBankAccountNumber(accountType);
        } while (bankAccountNumbersInDatabase.contains(accountNumber));
        return accountNumber;
    }

    public void addBankAccountToAccountHolder(Client accountHolder, BankAccount bankAccount){
        accountHolder.addBankAccount(bankAccount);
        bankAccount.addAccountHolder(accountHolder);
    }

    public Optional<BankAccount> findBankAccountByIban(String iban) {
        return bankAccountRepository.findBankAccountByIban(iban);
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
  public  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }


    public Optional<SmallBusinessBankAccount> findSmallBusinessBankAccountById(Long id) {
        return smallBusinessBankAccountRepository.findById(id);
    }


    public List<String> getAllAccountholderNamesByIBAN(String iban) {
        return bankAccountRepository.getAllAccountholderNamesByIBAN(iban);
    }
}
