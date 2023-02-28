package com.dellama.bank.webapi.DTO;

public class PinMachineConnectionDTO {


    private String iban;
    private int connectionCode;

    public PinMachineConnectionDTO(String iban, int connectionCode) {
        this.iban = iban;
        this.connectionCode = connectionCode;
    }

    public PinMachineConnectionDTO() {
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
        return "PinMachineConnectionDTO{" +
                "iban='" + iban + '\'' +
                ", connectionCode='" + connectionCode + '\'' +
                '}';
    }
}
