package com.dellama.bank.webapi.DTO;

/**
 * Response Object class for Login.
 */
public class LoginResponseDTO {
    private String id;
    private boolean isAccountManager;

    public LoginResponseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAccountManager() {
        return isAccountManager;
    }

    public void setAccountManager(boolean accountManager) {
        isAccountManager = accountManager;
    }

}
