package com.dellama.bank.pinterminal.model;

public class PinMachine {
    String pinMachineNumber;
    String ibanBankAccountNumber;
    public static int lastTransactionId;

    public PinMachine(String pinMachineNumber, String ibanBankAccountNumber, int lastTransactionId) {
        this.pinMachineNumber = pinMachineNumber;
        this.ibanBankAccountNumber = ibanBankAccountNumber;
        this.lastTransactionId = lastTransactionId;
    }

    public PinMachine() {
    }

    public String getPinMachineNumber() {
        return pinMachineNumber;
    }

    public void setPinMachineNumber(String pinMachineNumber) {
        this.pinMachineNumber = pinMachineNumber;
    }

    public String getIbanBankAccountNumber() {
        return ibanBankAccountNumber;
    }

    public void setIbanBankAccountNumber(String ibanBankAccountNumber) {
        this.ibanBankAccountNumber = ibanBankAccountNumber;
    }

    public boolean isConnected() {
        if (pinMachineNumber == null) {
            return false;
        } else if (pinMachineNumber.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public int getLastTransactionId() {
        return lastTransactionId;
    }

    public void newLastTransactionId() {
        this.lastTransactionId = ++lastTransactionId;
    }

    public void setLastTransactionId(int id) {
        this.lastTransactionId = id;
    }
}

