package com.dellama.bank.webapi.DTO;

import com.dellama.bank.webapi.model.approval.ApprovalStatus;

import java.util.Objects;

/**
 * Data Transfer Object class for PinMachineApproval
 */
public final class PinMachineApprovalDTO {
    private final Long id;
    private final Long clientId;
    private final String linkedBankAccountNumber;
    private final ApprovalStatus approvalStatus;

    public PinMachineApprovalDTO(Long id, Long clientId, String linkedBankAccountNumber, ApprovalStatus approvalStatus) {
        this.id = id;
        this.clientId = clientId;
        this.linkedBankAccountNumber = linkedBankAccountNumber;
        this.approvalStatus = approvalStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getLinkedBankAccountNumber() {
        return linkedBankAccountNumber;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PinMachineApprovalDTO that = (PinMachineApprovalDTO) o;
        return Objects.equals(getId(), that.getId())
               && getClientId().equals(that.getClientId())
               && linkedBankAccountNumber.equals(that.linkedBankAccountNumber)
               && getApprovalStatus() == that.getApprovalStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), linkedBankAccountNumber, getApprovalStatus());
    }

    @Override
    public String toString() {
        return "PinMachineApprovalDTO[" +
               "id=" + id + ", " +
               "clientId=" + clientId + ", " +
               "linkedBankAccountNumber=" + linkedBankAccountNumber + ", " +
               "approvalStatus=" + approvalStatus + ']';
    }

}
