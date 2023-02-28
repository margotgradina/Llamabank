package com.dellama.bank.webapi.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomBankAccountNumberGeneratorTest {
    /**
     * @verifies create a 10 digit number randomly
     * @see RandomBankAccountNumberGenerator#generateBankAccountNumber(String)
     */
    @Test
    public void generateBankAccountNumber_shouldCreateA10DigitNumberRandomly() throws Exception {
        String bankAccount = RandomBankAccountNumberGenerator.generateBankAccountNumber("smallBusinessAccount");
        Assertions.assertEquals(10, bankAccount.length());
    }

    /**
     * @verifies create a 10 digit number (returned in string) starting with 1 if account is SmallBusiness
     * @see RandomBankAccountNumberGenerator#generateBankAccountNumber(String)
     */
    @Test
    public void generateBankAccountNumber_shouldCreateA10DigitNumberReturnedInStringStartingWith1IfAccountIsSmallBusiness() throws Exception {
        String bankAccountNr = RandomBankAccountNumberGenerator.generateBankAccountNumber("smallBusinessAccount");
        boolean startsWith1 = bankAccountNr.startsWith("1");
        Assertions.assertEquals(true, startsWith1);
    }

    /**
     * @verifies create a 10 digit number (returned in string) starting with 2 if account is Private
     * @see RandomBankAccountNumberGenerator#generateBankAccountNumber(String)
     */
    @Test
    public void generateBankAccountNumber_shouldCreateA10DigitNumberReturnedInStringStartingWith2IfAccountIsPrivate() throws Exception {
        String bankAccountNr = RandomBankAccountNumberGenerator.generateBankAccountNumber("privateAccount");
        boolean startsWith2 = bankAccountNr.startsWith("2");
        Assertions.assertEquals(true, startsWith2);
    }
}
