package com.dellama.bank.webapi.model.approval;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of the statuses an approval can be in
 */
public enum ApprovalStatus {
    PENDING("pending"),
    APPROVED("approved"),
    DENIED("denied");

    private final String statusName;

    ApprovalStatus(String statusName) {
        this.statusName = statusName;
    }

    @JsonValue
    public String getStatusName() {
        return statusName;
    }
}
