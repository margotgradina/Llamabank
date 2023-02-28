package com.dellama.bank.webapi.model;

public enum Sector {
    CON("Construction"),
    CSR("Culture, sport, recreation"),
    RET("Retail"),
    EWE("Energy, water, environment"),
    FIN("Finance"),
    HEA("Healthcare"),
    WHO("Wholesale"),
    HOS("Hospitality"),
    ICT("ICT and media"),
    IND("Industry"),
    LOG("Logistics"),
    OTH("Other"),
    PES("Personal services"),
    BUS("Business services");

    private final String statusName;

    Sector(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}


