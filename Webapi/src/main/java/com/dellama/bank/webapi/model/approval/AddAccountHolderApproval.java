package com.dellama.bank.webapi.model.approval;

import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.Client;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * The AddAccountHolderApproval represents an approval request to add another client as account holder to a bank account
 */
@Entity
public class AddAccountHolderApproval extends Approval {
    @ManyToOne
    private Client newAccountHolder;

    @ManyToOne
    private BankAccount bankAccount;

    private String securityCode;

    public AddAccountHolderApproval() {
    }

    public AddAccountHolderApproval(Client client,
                                    Client newAccountHolder,
                                    BankAccount bankAccount,
                                    String securityCode) {
        super(bankAccount instanceof SmallBusinessBankAccount, client, ApprovalStatus.PENDING);
        this.newAccountHolder = newAccountHolder;
        this.bankAccount = bankAccount;
        this.securityCode = securityCode;
    }

    public Client getNewAccountHolder() {
        return newAccountHolder;
    }

    public void setNewAccountHolder(Client newAccountHolder) {
        this.newAccountHolder = newAccountHolder;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AddAccountHolderApproval that = (AddAccountHolderApproval) o;
        return getNewAccountHolder().equals(that.getNewAccountHolder())
               && getBankAccount().equals(that.getBankAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNewAccountHolder(), getBankAccount());
    }
}
