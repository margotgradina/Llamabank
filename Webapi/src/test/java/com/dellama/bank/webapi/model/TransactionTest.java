package com.dellama.bank.webapi.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TransactionTest {

    private final static String VALID_IBAN_BANKACCOUNT_NR = "NL20INGB9904789940";
    private final static String VALID_IBAN_BANKACCOUNT_NR2 = "NL50RABO9661117578";
    private final static int VALID_AMOUNT = 100;
    private final static String MESSAGE = "berichtje";
    private final static LocalDateTime DATE_TIME = LocalDateTime.now();

    /**
     * @verifies create a transaction with valid parameters
     * @see Transaction#Transaction(String, String, double, String, java.time.LocalDateTime)
     */
    @Test
    public void Transaction_shouldCreateATransactionWithValidParameters() throws Exception {
        Transaction sut = new Transaction(VALID_IBAN_BANKACCOUNT_NR, VALID_IBAN_BANKACCOUNT_NR2, VALID_AMOUNT, MESSAGE, DATE_TIME);
        Assertions.assertEquals(VALID_IBAN_BANKACCOUNT_NR, sut.getFromIBANBankAccountNr());
        Assertions.assertEquals(VALID_IBAN_BANKACCOUNT_NR2, sut.getToIBANBankAccountNr());
        Assertions.assertEquals(VALID_AMOUNT, sut.getAmount());
        Assertions.assertEquals(MESSAGE, sut.getMessage());
        Assertions.assertEquals(DATE_TIME, sut.getDateTime());
    }

    /**
     * @verifies throw IllegalArgumentException if fromIBANBankAccountNr is null or blank
     * @see Transaction#Transaction(String, String, double, String, java.time.LocalDateTime)
     */
    @ParameterizedTest
    @MethodSource("checkForNullOrBlankInput")
    public void Transaction_shouldThrowIllegalArgumentExceptionIfFromIBANBankAccountNrIsNullOrBlank(String fromIBANBankAccountNr) throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Transaction(fromIBANBankAccountNr, VALID_IBAN_BANKACCOUNT_NR, VALID_AMOUNT, MESSAGE, DATE_TIME));
    }

    /**
     * @verifies throw IllegalArgumentException if toIBANBankAccountNr is null or blank
     * @see Transaction#Transaction(String, String, double, String, java.time.LocalDateTime)
     */
    @ParameterizedTest
    @MethodSource("checkForNullOrBlankInput")
    public void Transaction_shouldThrowIllegalArgumentExceptionIfToIBANBankAccountNrIsNullOrBlank(String toIBANBankAccountNr) throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Transaction(VALID_IBAN_BANKACCOUNT_NR, toIBANBankAccountNr, VALID_AMOUNT, MESSAGE, DATE_TIME));
    }

    private static Stream<String> checkForNullOrBlankInput() {
        return Stream.of("", null);
    }

    /**
     * @verifies throw IllegalArgumentException if toIBANBankAccountNr is equal to fromIBANBankAccountNr
     * @see Transaction#Transaction(String, String, double, String, LocalDateTime)
     */
    @Test
    public void Transaction_shouldThrowIllegalArgumentExceptionIfToIBANBankAccountNrIsEqualToFromIBANBankAccountNr() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Transaction(VALID_IBAN_BANKACCOUNT_NR, VALID_IBAN_BANKACCOUNT_NR, VALID_AMOUNT, MESSAGE, DATE_TIME));
    }
}
