package com.dellama.bank.webapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

public class PinMachineCheckConnectionDTO {


    private String message;


    private boolean isConnectionApproved;


    private int identificationNumber;

    public PinMachineCheckConnectionDTO(@Nullable String message, boolean isConnectionApproved, int identificationNumber) {
        this.setMessage(message);
        this.setConnectionApproved(isConnectionApproved);
        this.setIdentificationNumber(identificationNumber);
    }

    public PinMachineCheckConnectionDTO(@Nullable String message, boolean isConnectionApproved) {
        this.setMessage(message);
        this.setConnectionApproved(isConnectionApproved);
        this.setIdentificationNumber(0);

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsConnectionApproved() {
        return isConnectionApproved;
    }

    public void setConnectionApproved(boolean connectionApproved) {
        isConnectionApproved = connectionApproved;
    }

    public int getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(int identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
}
