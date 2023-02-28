package com.dellama.bank.webapi.DTO;

import com.dellama.bank.webapi.model.approval.ApprovalStatus;

import java.util.Objects;

/**
 * Data Transfer Object class for AddAccountHolderApproval
 */
public final class AddAccountHolderApprovalDTO {
    private  Long id;
    private  Long clientId;
    private  Long newAccountHolderId;
    private  String bankAccountNumber;
    private  String securityCode;
    private  ApprovalStatus approvalStatus;

    public AddAccountHolderApprovalDTO(Long id,
                                       Long clientId,
                                       Long newAccountHolderId,
                                       String bankAccountNumber,
                                       String securityCode, ApprovalStatus approvalStatus) {
        this.id = id;
        this.clientId = clientId;
        this.newAccountHolderId = newAccountHolderId;
        this.bankAccountNumber = bankAccountNumber;
        this.securityCode = securityCode;
        this.approvalStatus = approvalStatus;
    }

    public AddAccountHolderApprovalDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getNewAccountHolderId() {
        return newAccountHolderId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setNewAccountHolderId(Long newAccountHolderId) {
        this.newAccountHolderId = newAccountHolderId;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddAccountHolderApprovalDTO that = (AddAccountHolderApprovalDTO) o;
        return Objects.equals(getId(), that.getId())
               && getClientId().equals(that.getClientId())
               && getNewAccountHolderId().equals(that.getNewAccountHolderId())
               && getBankAccountNumber().equals(that.getBankAccountNumber())
               && getSecurityCode().equals(that.getSecurityCode())
               && getApprovalStatus() == that.getApprovalStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                            getClientId(),
                            getNewAccountHolderId(),
                            getBankAccountNumber(),
                            getSecurityCode(),
                            getApprovalStatus());
    }

    @Override
    public String toString() {
        return "AddAccountHolderApprovalDTO["
               + "id="
               + id
               + ", "
               + "clientId="
               + clientId
               + ", "
               + "newAccountHolderId="
               + newAccountHolderId
               + ", "
               + "bankAccountNumber="
               + bankAccountNumber
               + ", "
               + "securityCode="
               + securityCode
               + ", "
               + ']';
    }

}
