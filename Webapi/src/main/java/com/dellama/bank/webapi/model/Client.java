package com.dellama.bank.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client extends UserAccount {

    private String email;
    private String phoneNumber;
    private String bsn;

    @ManyToMany
    @JoinTable(name = "client_holds_bankaccount",
               joinColumns = @JoinColumn(name = "client_id"),
               inverseJoinColumns = @JoinColumn(name = "bank_account_id"))
    @JsonIgnore
    private Set<BankAccount> bankAccounts;

    @ManyToOne
    private Address address;

    public Client(String userName, String password, String name, String email, String phoneNumber, String bsn, Address address) {
        super(userName, password, name);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bsn = bsn;
        this.address = address;
    }

    public Client() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return bsn.equals(client.bsn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bsn);
    }

    //region get/setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String BSN) {
        this.bsn = BSN;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addBankAccount(BankAccount bankAccount) {
        if (this.bankAccounts == null) {
            this.bankAccounts = new HashSet<>();
        }
        this.bankAccounts.add(bankAccount);
    }

//endregion

    @Override
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", BSN='" + bsn + '\'' +
                ", bankAccounts=" + bankAccounts +
                "} " + super.toString();
    }
}
