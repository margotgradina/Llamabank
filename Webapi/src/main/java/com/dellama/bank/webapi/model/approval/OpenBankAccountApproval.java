package com.dellama.bank.webapi.model.approval;

import com.dellama.bank.webapi.model.*;

import javax.persistence.Entity;

/**
 * The OpenBankAccountApproval class represents an approval request to open a new bank account
 */
@Entity
public class OpenBankAccountApproval extends Approval {
    private String bankAccountNumber;

    private OpenBankAccountType openBankAccountType;

    private Sector smallBusinessSector;

    public OpenBankAccountApproval() {
    }

    public OpenBankAccountApproval(Client client, OpenBankAccountType openBankAccountType, Sector smallBusinessSector) {
        super(smallBusinessSector != null, client, ApprovalStatus.PENDING);
        this.openBankAccountType = openBankAccountType;
        if (openBankAccountType.equals(OpenBankAccountType.BUSINESS)) {
            this.smallBusinessSector = smallBusinessSector;
        }
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public OpenBankAccountType getOpenBankAccountType() {
        return openBankAccountType;
    }

    public void setOpenBankAccountType(OpenBankAccountType openBankAccountType) {
        this.openBankAccountType = openBankAccountType;
    }

    public Sector getSmallBusinessSector() {
        return smallBusinessSector;
    }

    public void setSmallBusinessSector(Sector smallBusinessSector) {
        this.smallBusinessSector = smallBusinessSector;
    }
}
