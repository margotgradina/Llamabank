package com.dellama.bank.webapi.DTO;

import com.dellama.bank.webapi.model.approval.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ApprovalInfoDTO {

    @JsonProperty("Approval id")
    private Long id;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Client")
    private String clientName;

    @JsonProperty("Created on")
    private LocalDateTime creationTime;

    public ApprovalInfoDTO() {
    }

    public static ApprovalInfoDTO approvalInfoDTOFromApproval(Approval approval) {
        var approvalInfoDTO = new ApprovalInfoDTO();
        approvalInfoDTO.setId(approval.getId());
        approvalInfoDTO.setClientName(approval.getClient().getName());
        approvalInfoDTO.setCreationTime(approval.getCreationTime());

        // I want my pattern matching in switches and I want it now!!!!!!!
        if (approval instanceof AddAccountHolderApproval) approvalInfoDTO.setType("Add Account Holder");
        if (approval instanceof OpenBankAccountApproval) approvalInfoDTO.setType("Open Bank Account");
        if (approval instanceof PinMachineApproval) approvalInfoDTO.setType("Pin Machine");

        return approvalInfoDTO;
        }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
