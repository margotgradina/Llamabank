package com.dellama.bank.webapi.model.approval;

import com.dellama.bank.webapi.model.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * The Approval class represents a customer request that needs to be checked by an account manager
 * It can be approved or denied, and the approve() and deny() methods need to be implemented by the
 * specific implementations
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = AddAccountHolderApproval.class, name = "AddAccountHolderApproval"),
              @JsonSubTypes.Type(value = OpenBankAccountApproval.class, name = "OpenBankAccountApproval"),
              @JsonSubTypes.Type(value = PinMachineApproval.class, name = "PinMachineApproval")})
public abstract class Approval implements Comparable<Approval> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isRequestFromSmallBusiness;

    private final LocalDateTime creationTime = LocalDateTime.now();

    @ManyToOne
    private Client client;

    private ApprovalStatus approvalStatus;

    private boolean consumed;

    protected Approval() {
    }


    protected Approval(boolean isRequestFromSmallBusiness, Client client, ApprovalStatus approvalStatus) {
        this.isRequestFromSmallBusiness = isRequestFromSmallBusiness;
        this.client = client;
        this.approvalStatus = approvalStatus;
        this.consumed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRequestFromSmallBusiness() {
        return isRequestFromSmallBusiness;
    }

    public void setRequestFromSmallBusiness(boolean requestFromSmallBusiness) {
        isRequestFromSmallBusiness = requestFromSmallBusiness;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @JsonProperty("approvalStatus")
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Approval approval = (Approval) o;
        return getCreationTime().equals(approval.getCreationTime())
               && getClient().equals(approval.getClient())
               && getApprovalStatus() == approval.getApprovalStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreationTime(), getClient(), getApprovalStatus());
    }

    @Override
    public int compareTo(Approval o) {
        return this.creationTime.compareTo(o.creationTime);
    }
}
