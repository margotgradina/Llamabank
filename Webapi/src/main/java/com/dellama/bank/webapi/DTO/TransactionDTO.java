package com.dellama.bank.webapi.DTO;


public class TransactionDTO {

    private String message;
    private Double amount;
    private String fromIBANBankAccountNr;
    private String toIBANBankAccountNr;

    public TransactionDTO(String message, Double amount, String fromIBANBankAccountNr, String toIBANBankAccountNr) {
        this.message = message;
        this.amount = amount;
        this.fromIBANBankAccountNr = fromIBANBankAccountNr;
        this.toIBANBankAccountNr = toIBANBankAccountNr;
    }

    //region Getters en Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFromIBANBankAccountNr() {
        return fromIBANBankAccountNr;
    }

    public void setFromIBANBankAccountNr(String fromIBANBankAccountNr) {
        this.fromIBANBankAccountNr = fromIBANBankAccountNr;
    }

    public String getToIBANBankAccountNr() {
        return toIBANBankAccountNr;
    }

    public void setToIBANBankAccountNr(String toIBANBankAccountNr) {
        this.toIBANBankAccountNr = toIBANBankAccountNr;
    }
    //endregion

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "message='" + message + '\'' +
                ", amount=" + amount +
                ", fromIBANBankAccountNr='" + fromIBANBankAccountNr + '\'' +
                ", toIBANBankAccountNr='" + toIBANBankAccountNr + '\'' +
                '}';
    }
}
