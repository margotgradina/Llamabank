package com.dellama.bank.webapi.model;

import javax.persistence.Entity;
import java.util.List;
import java.util.Set;


@Entity
public class PrivateBankAccount extends BankAccount {


    public PrivateBankAccount(String accountNumber, String name, double balance, Set<Client> accountHolders) {
        super(accountNumber, name, balance, accountHolders);
    }

    public PrivateBankAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
    }

    public PrivateBankAccount(String accountNumber, Client client) {
        super(accountNumber, client);
        setName(String.format("Private bank account for %s", client.getName()));
    }

    public PrivateBankAccount() {
    }

}
