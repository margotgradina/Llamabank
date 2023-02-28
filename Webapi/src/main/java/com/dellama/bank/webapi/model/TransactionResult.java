package com.dellama.bank.webapi.model;

public enum TransactionResult {
    BALANCE_ERROR("Balance too low."),
    SAME_ACCOUNT_ERROR("The debit and credit accounts are equal."),
    AMOUNT_ERROR("Amount must be higher than 0."),
    TRANSACTION_OK("Transaction successful."),
    ACCOUNT_ERROR("Account does not exist.");

    private final String transactionStatus;

    TransactionResult(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }
}
