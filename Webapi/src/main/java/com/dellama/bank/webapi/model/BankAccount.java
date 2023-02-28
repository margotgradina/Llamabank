package com.dellama.bank.webapi.model;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //added unique key, accountNumber must be unique
    @Column(unique = true)
    private String accountNumber;

    private String iban;

    private String name;
    private double balance;

    @ManyToMany(mappedBy = "bankAccounts")
    @JsonIgnore
    private Set<Client> accountHolders;


    /**
     * @param accountNumber
     * @param name
     * @param balance
     * @param accountHolders
     */
    public BankAccount(String accountNumber, String name, double balance, Set<Client> accountHolders) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.accountHolders = accountHolders;

    }


    /**
     * @param accountNumber
     * @param name
     * @param balance
     */

    public BankAccount(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    /**
     * Creates a new BankAccount with the given client as its sole accountHolder
     * @param accountNumber
     * @param client
     */
    public BankAccount(String accountNumber, Client client) {
        this.accountNumber = accountNumber;
        this.accountHolders = new HashSet<>();
        this.accountHolders.add(client);
        client.addBankAccount(this);
    }

    public BankAccount() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<Client> getAccountHolders() {
        return accountHolders;
    }

    public void setAccountHolders(Set<Client> accountHolders) {
        this.accountHolders = accountHolders;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void addAccountHolder(Client client) {
        if ( this.accountHolders == null ) {
            this.accountHolders = new HashSet<>();
        }
        accountHolders.add(client);
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BankAccount that = (BankAccount) o;
        return accountNumber.equals(that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }



}
