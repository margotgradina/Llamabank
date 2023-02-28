package com.dellama.bank.webapi.model;

import javax.persistence.Entity;
import java.util.List;
import java.util.Set;

@Entity
public class SmallBusinessBankAccount extends BankAccount {
    Sector sector;

    public SmallBusinessBankAccount(String accountNumber, String name, double balance, Set<Client> accountHolders, Sector sector) {
        super(accountNumber, name, balance, accountHolders);
        this.sector = sector;
    }

    public SmallBusinessBankAccount(String accountNumber, String name, double balance, Sector sector) {
        super(accountNumber, name, balance);
        this.sector = sector;
    }

    public SmallBusinessBankAccount(String accountNumber, Client client, Sector sector) {
        super(accountNumber, client);
        setName(String.format("Small Business Bank Account for %s", client.getName()));
        this.sector = sector;
    }

    public SmallBusinessBankAccount() {
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
