package com.dellama.bank.webapi.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccountDTO {
    @JsonProperty("Bank account number")
    private String accountNumber;

    @JsonProperty("Iban")
    private String iban;

    @JsonProperty("Balance")
    private double balance;

    @JsonProperty("Account Name")
    private String name;


    @JsonProperty("Sector")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sector;

    public BankAccountDTO(String accountNumber, double balance, String name, String sector, String iban) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.name = name;
        this.sector = sector;
        this.iban = iban;
    }



    public BankAccountDTO() {
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
