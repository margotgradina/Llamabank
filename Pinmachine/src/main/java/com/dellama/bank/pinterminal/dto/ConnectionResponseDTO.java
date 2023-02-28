package com.dellama.bank.pinterminal.dto;

public class ConnectionResponseDTO {
    public String message;
    public boolean isConnectionApproved;
    public int identificationNumber;

    public ConnectionResponseDTO(String message, boolean isConnectionApproved, int identificationNumber) {
        this.message = message;
        this.isConnectionApproved = isConnectionApproved;
        this.identificationNumber = identificationNumber;
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

    @Override
    public String toString() {
        return "ConnectionResponseDTO{" +
                "message='" + message + '\'' +
                ", isConnectionApproved=" + isConnectionApproved +
                ", identificationNumber=" + identificationNumber +
                '}';
    }
}
