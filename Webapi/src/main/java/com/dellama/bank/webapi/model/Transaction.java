package com.dellama.bank.webapi.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromIBANBankAccountNr;
    private String toIBANBankAccountNr;

    private double amount;
    private String message;
    private LocalDateTime dateTime;


    /**
     * All-args constructor for Transaction.
     *
     * @param fromIBANBankAccountNr the bankaccountnr where the money comes from
     * @param toIBANBankAccountNr   the bankaccountnr where the money goes to
     * @param amount                the amount of money which is being booked
     * @param message               the optional message provided with the transaction
     * @param dateTime              the date and time of the transaction
     * @throws IllegalArgumentException if fromIBANBankAccountNr is null or blank
     * @throws IllegalArgumentException if toIBANBankAccountNr is null or blank
     * @throws IllegalArgumentException if amount is < 0
     * @throws IllegalArgumentException if fromIBANBankAccountNr and toIBANBankAccountNr is equal
     * @should create a transaction with valid parameters
     * @should throw IllegalArgumentException if fromIBANBankAccountNr is null or blank
     * @should throw IllegalArgumentException if toIBANBankAccountNr is null or blank
     * @should throw IllegalArgumentException if toIBANBankAccountNr is equal to fromIBANBankAccountNr
     */

    public Transaction(String fromIBANBankAccountNr, String toIBANBankAccountNr, double amount, String message, LocalDateTime dateTime) throws IllegalArgumentException {
        if (fromIBANBankAccountNr == null || fromIBANBankAccountNr.isBlank()) {
            throw new IllegalArgumentException("fromIBANBankAccountNr can't be null/empty");
        }
        if (toIBANBankAccountNr == null || toIBANBankAccountNr.isBlank()) {
            throw new IllegalArgumentException("toIBANBankAccountNr can't be null/empty");
        }
        if (toIBANBankAccountNr.equals(fromIBANBankAccountNr)) {
            throw new IllegalArgumentException("You can't transfer money to the same bankaccount...");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount can't be negative");
        }
        this.fromIBANBankAccountNr = fromIBANBankAccountNr;
        this.toIBANBankAccountNr = toIBANBankAccountNr;
        this.amount = amount;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Transaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromBankAccount=" + fromIBANBankAccountNr +
                ", toBankAccount=" + toIBANBankAccountNr +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    //region Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    //endregion
}
