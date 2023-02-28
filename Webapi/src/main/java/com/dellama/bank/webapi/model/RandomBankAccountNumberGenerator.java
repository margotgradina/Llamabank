package com.dellama.bank.webapi.model;

public class RandomBankAccountNumberGenerator {
    private String bankAccountNumber;

    /**
     * @param typeAccount
     * @return a random 10 digit number in String
     * @should create a 10 digit number randomly
     * @should create a 10 digit number (returned in string) starting with 1 if account is SmallBusiness
     * @should create a 10 digit number (returned in string) starting with 2 if account is Private
     */
    public static String generateBankAccountNumber(String typeAccount) {
        StringBuilder sB = new StringBuilder();
        boolean uniqueNumber = false;

        if (typeAccount.equals("smallBusinessAccount")) {
            sB.append(1);
        } else {
            sB.append(2);
        }

        for (int i = 0; i < 9; i++) {
            int nextNumber = (int) (Math.random() * 10);
            sB.append(nextNumber);
        }

        return sB.toString();
    }
}
