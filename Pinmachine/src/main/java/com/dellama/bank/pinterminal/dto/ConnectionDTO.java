package com.dellama.bank.pinterminal.dto;

import com.google.gson.annotations.JsonAdapter;

public class ConnectionDTO {

    public String iban;
    public int connectionCode;

    public ConnectionDTO(String iban, int connectionCode) {
        this.iban = iban;
        this.connectionCode = connectionCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(int connectionCode) {
        this.connectionCode = connectionCode;
    }

    @Override
    public String toString() {
        return "ConnectionDTO{" + "iban='" + iban + '\'' + ", connectionCode=" + connectionCode + '}';
    }
}
