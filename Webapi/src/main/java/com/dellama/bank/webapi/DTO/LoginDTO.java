package com.dellama.bank.webapi.DTO;

/**
 * Data Transfer Object class for Login
 */
public class LoginDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                ", passWord='" + password + '\'' +
                '}';
    }
}
