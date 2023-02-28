package com.dellama.bank.webapi.model.iban;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class IbanUtilsTest {
    private static final String INTERNAL_BANK_ACCOUNT_NUMBER = "1234567890";
    private static final String VALID_INTERNAL_IBAN_CHECKSUM = "15";
    private static final String VALID_INTERNAL_IBAN = "NL15LAMA1234567890";
    private static final String VALID_EXTERNAL_IBAN = "NL33BOBO1234567890";
    private static final String INVALID_INTERNAL_IBAN = "NL20LAMA1234567890";
    private static final String INCORRECT_LENGTH_INTERNAL_IBAN = "NL20LAMA12345678901";
    
    public static final String UNIT = "unit";

    @Test
    @Tag(UNIT)
    void isValidInternalIbanReturnsFalseIfIbanIsNull() {
        assertFalse(IbanUtils.isValidInternalIban(null));
    }

    @Test
    @Tag(UNIT)
    void isValidInternalIbanReturnsFalseForExternalIban() {
        assertFalse(IbanUtils.isValidInternalIban(VALID_EXTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void isValidInternalIbanReturnsTrueForValidInternalIban() {
        assertTrue(IbanUtils.isValidInternalIban(VALID_INTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void isValidInternalIbanReturnsFalseForInvalidInternalIban() {
        assertFalse(IbanUtils.isValidInternalIban(INVALID_INTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void isValidInternalIbanReturnsFalseForIncorrectLengthNLIban() {
        assertFalse(IbanUtils.isValidInternalIban(INCORRECT_LENGTH_INTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void calculateChecksumThrowsIllegalArgumentExceptions() {
        assertThrows(IllegalArgumentException.class, () -> IbanUtils.calculateChecksum(null));
        assertThrows(IllegalArgumentException.class, () -> IbanUtils.calculateChecksum(INTERNAL_BANK_ACCOUNT_NUMBER + "1"));
    }

    @Test
    @Tag(UNIT)
    void calculateChecksumCorrectlyCalculatesChecksumForInternalIban() {
        assertEquals(VALID_INTERNAL_IBAN_CHECKSUM, IbanUtils.calculateChecksum(INTERNAL_BANK_ACCOUNT_NUMBER));
    }

    @Test
    @Tag(UNIT)
    void internalBankAccountNumberToIbanThrowsIllegalArgumentExceptions() {
        assertThrows(IllegalArgumentException.class, () -> IbanUtils.internalBankAccountNumberToIban(null));
        assertThrows(IllegalArgumentException.class, () -> IbanUtils.internalBankAccountNumberToIban(INCORRECT_LENGTH_INTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void internalBankAccountNumberToIbanReturnsCorrectIBAN() {
        assertEquals(VALID_INTERNAL_IBAN, IbanUtils.internalBankAccountNumberToIban(INTERNAL_BANK_ACCOUNT_NUMBER));
    }

    @Test
    @Tag(UNIT)
    void IbanToInternalBankAccountNumberThrowsIllegalArgumentExceptions() {
        assertThrows(IllegalArgumentException.class,
                     () -> IbanUtils.ibanToInternalBankAccountNumber(VALID_EXTERNAL_IBAN));
    }

    @Test
    @Tag(UNIT)
    void IbanToInternalBankAccountNumberReturnsCorrectInternalBankAccountNumber() {
        assertEquals(INTERNAL_BANK_ACCOUNT_NUMBER, IbanUtils.ibanToInternalBankAccountNumber(VALID_INTERNAL_IBAN));
    }
}