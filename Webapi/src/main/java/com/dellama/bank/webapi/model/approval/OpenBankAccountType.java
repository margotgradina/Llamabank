package com.dellama.bank.webapi.model.approval;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of bank account types
 */
public enum OpenBankAccountType {
    PRIVATE("private"),
    BUSINESS("business");

    private final String bankAccountType;

    OpenBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    @JsonValue
    public String getBankAccountType() {
        return bankAccountType;
    }
}
