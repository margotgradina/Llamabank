package com.dellama.bank.webapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configuration")
public class PropertiesController {

    @Autowired
    private BankProperty bankProperty;

    @CrossOrigin
    @RequestMapping("properties")
    public BankProperty getProperties() {
        //Thread.sleep(3000); //artificially slowdown backend to test async/await fetch in frontend
        return bankProperty;
    }
}


@Component
@ConfigurationProperties(prefix = "bank")
class BankProperty {

    // Add new properties both here and in application.properties
    @Value("${server.port}")
    private String serverPort;

    private String bankName;
    private String bankNameUpperCase;
    private String bankCode;
    private String bankSlogan;
    private String bankHostName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNameUpperCase() {
        return bankNameUpperCase;
    }

    public void setBankNameUpperCase(String bankNameUpperCase) {
        this.bankNameUpperCase = bankNameUpperCase;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankSlogan() {
        return bankSlogan;
    }

    public void setBankSlogan(String bankSlogan) {
        this.bankSlogan = bankSlogan;
    }

    public String getBankHostName() {
        return bankHostName;
    }

    public void setBankHostName(String bankHostName) {
        this.bankHostName = bankHostName;
    }

    public String getBankFullApiUrl() {
        return getBankHostName() + ":" + serverPort;
    }
}
