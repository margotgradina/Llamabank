package com.dellama.bank.webapi.DTO;

import com.dellama.bank.webapi.model.approval.ApprovalStatus;

import java.util.Objects;

/**
 * Data Transfer Object for ApprovalStatus
 */
public final class ApprovalStatusDTO {
    private  Long approvalId;
    private ApprovalStatus approvalStatus;

    /**
     * @param approvalId the Id of the Approval
     * @param approvalStatus the status of the Approval
     */
    public ApprovalStatusDTO(Long approvalId, ApprovalStatus approvalStatus) {
        this.approvalId = approvalId;
        this.approvalStatus = approvalStatus;
    }

    public ApprovalStatusDTO() {
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ApprovalStatusDTO) obj;
        return Objects.equals(this.approvalId, that.approvalId) &&
               Objects.equals(this.approvalStatus, that.approvalStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvalId, approvalStatus);
    }

    @Override
    public String toString() {
        return "ApprovalStatusDTO[" +
               "approvalId=" + approvalId + ", " +
               "approvalStatus=" + approvalStatus + ']';
    }

}
