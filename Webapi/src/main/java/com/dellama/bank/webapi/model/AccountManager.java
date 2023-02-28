package com.dellama.bank.webapi.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class AccountManager extends UserAccount {

    private boolean isSmallBusinessManager;

    public AccountManager(String userName, String password, String name, boolean isSmallBusinessManager) {
        super(userName, password, name);
        this.isSmallBusinessManager = isSmallBusinessManager;
    }

    public AccountManager(){
    }

    //region get/setters
    public boolean isSmallBusinessManager() {
        return isSmallBusinessManager;
    }

    public void setSmallBusinessManager(boolean smallBusinessManager) {
        isSmallBusinessManager = smallBusinessManager;
    }
    //endregion

    @Override
    public String toString() {
        return "AccountManager{" +
                "isSmallBusinessManager=" + isSmallBusinessManager +
                "} " + super.toString();
    }
}
