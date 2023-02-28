package com.dellama.bank.webapi.model.approval;

import com.dellama.bank.webapi.model.Client;
import com.dellama.bank.webapi.model.PinMachine;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * The PinMachineApproval class represents an approval request to get a pin machine for the specified Small Business
 * bank account
 */
@Entity
public class PinMachineApproval extends Approval {
    @OneToOne
    private PinMachine pinMachine;

    @ManyToOne
    private SmallBusinessBankAccount linkedBankAccount;

    public PinMachineApproval() {
    }

    public PinMachineApproval(Client client,
                              SmallBusinessBankAccount linkedBankAccount) {
        super(true, client, ApprovalStatus.PENDING);
        this.linkedBankAccount = linkedBankAccount;
    }

    public PinMachine getPinMachine() {
        return pinMachine;
    }

    public void setPinMachine(PinMachine pinMachine) {
        this.pinMachine = pinMachine;
    }

    public SmallBusinessBankAccount getLinkedBankAccount() {
        return linkedBankAccount;
    }

    public void setLinkedBankAccount(SmallBusinessBankAccount linkedBankAccount) {
        this.linkedBankAccount = linkedBankAccount;
    }
}
