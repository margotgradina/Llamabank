package com.dellama.bank.webapi.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class PinMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column

    private long identificationNumberPinMachine;

    @Column

    private int connectionCode;

    @ManyToOne
    private SmallBusinessBankAccount smallBusinessBankAccount;

    public PinMachine(SmallBusinessBankAccount smallBusinessBankAccount, int connectionCode, long identificationNumberPinMachine) {
        this.smallBusinessBankAccount = smallBusinessBankAccount;
        this.connectionCode = connectionCode;
        this.identificationNumberPinMachine = identificationNumberPinMachine;
    }


    public PinMachine(int connectionCode, SmallBusinessBankAccount smallBusinessBankAccount) {
        this.connectionCode = connectionCode;
        this.smallBusinessBankAccount = smallBusinessBankAccount;
    }

    public PinMachine(SmallBusinessBankAccount smallBusinessBankAccount) {
        this.smallBusinessBankAccount = smallBusinessBankAccount;
    }

    public PinMachine() {

    }

    public int getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(int connectionCode) {
        this.connectionCode = connectionCode;
    }

    public long getIdentificationNumberPinMachine() {
        return identificationNumberPinMachine;
    }

    public void setIdentificationNumberPinMachine(long identificationNumberPinMachine) {
        this.identificationNumberPinMachine = identificationNumberPinMachine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SmallBusinessBankAccount getSmallBusinessBankAccount() {
        return smallBusinessBankAccount;
    }

    public void setSmallBusinessBankAccount(SmallBusinessBankAccount smallBusinessBankAccount) {
        this.smallBusinessBankAccount = smallBusinessBankAccount;
    }
}
