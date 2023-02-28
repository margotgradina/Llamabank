package com.dellama.bank.webapi.DTO;

import com.dellama.bank.webapi.model.Sector;
import com.dellama.bank.webapi.model.approval.ApprovalStatus;

import java.util.Objects;

/**
 * Data Transfer Object class for OpenBankAccountApproval
 */
public class OpenBankAccountApprovalDTO {
    private Long id;
    private Long clientId;
    private Sector sector;
    private ApprovalStatus approvalStatus;

    public OpenBankAccountApprovalDTO(Long id, Long clientId, Sector sector, ApprovalStatus approvalStatus) {
        this.id = id;
        this.clientId = clientId;
        this.sector = sector;
        this.approvalStatus = approvalStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Sector getSector() {
        return sector;
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
        OpenBankAccountApprovalDTO that = (OpenBankAccountApprovalDTO) o;
        return Objects.equals(getId(), that.getId())
               && getClientId().equals(that.getClientId())
               && getSector() == that.getSector()
               && getApprovalStatus() == that.getApprovalStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getSector(), getApprovalStatus());
    }

    @Override
    public String toString() {
        return "OpenBankAccountApprovalDTO{" +
               "id=" + id +
               ", clientId=" + clientId +
               ", sector=" + sector +
               ", approvalStatus=" + approvalStatus +
               '}';
    }
}
