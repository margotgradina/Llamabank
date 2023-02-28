package com.dellama.bank.webapi.model.iban;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * The IbanUtils class provides utilities for validating Llama Bank IBANs and converting between internal
 * bank account numbers and IBANs
 */
public final class IbanUtils {
    private static final String LLAMA_COUNTRY_CODE = "NL";
    private static final BbanFormat LLAMA_BBAN_FORMAT = new BbanFormat(4, 10);

    @Value("${bank.bankcode}")
    private static String llamaBankCode = "LAMA"; // this probably needs to be changed

    /**
     * This class cannot be instantiated
     */
    private IbanUtils() {
    }

    /**
     * Checks if the provided IBAN passes the IBAN verification and is a valid IBAN for Llama Bank
     * Note: does NOT check the persistence layer to see if the bank account is actually present
     *
     * @param iban the IBAN to check
     * @return true if the IBAN passes the IBAN verification, false otherwise
     */
    public static boolean isValidInternalIban(String iban) {
        if (iban == null) {
            return false;
        }

        iban = iban.toUpperCase().replace(" ", "");

        if (iban.length() != LLAMA_BBAN_FORMAT.bankCodeLength() + LLAMA_BBAN_FORMAT.accountNumberLength() + 4) {
            return false;
        }

        if (containsNonAlphanumericCharacters(iban)) {
            return false;
        }

        int checkSum = Integer.parseInt(iban.substring(2, 4));

        if (checkSum < 2 || checkSum > 98) {
            return false;
        }

        BigInteger numericalIban = new BigInteger(ibanToNumericalIban(iban));

        boolean passesIbanVerification = numericalIban.mod(BigInteger.valueOf(97)).equals(BigInteger.ONE);
        return passesIbanVerification && iban.startsWith(LLAMA_COUNTRY_CODE) && iban.startsWith(llamaBankCode, 4);
    }

    /**
     * Calculates the IBAN checksum for an internal bank account number of Llama Bank
     *
     * @param accountNumber the internal Llama Bank bank account number
     * @return the IBAN checksum
     * @throws IllegalArgumentException if accountNumber is null, has invalid length as defined in
     *                                  LLAMA_BBAN_FORMAT.accountNumberLength, or contains non-alphanumeric characters
     */
    public static String calculateChecksum(String accountNumber) throws IllegalArgumentException {
        if (accountNumber == null || accountNumber.length() != LLAMA_BBAN_FORMAT.accountNumberLength()) {
            throw new IllegalArgumentException("Incorrect account number length!");
        }

        accountNumber = accountNumber.replace(" ", "");

        if (containsNonAlphanumericCharacters(accountNumber)) {
            throw new IllegalArgumentException("Account number can only contain alphanumeric characters");
        }

        var rearrangedAccountNumber = llamaBankCode + accountNumber + LLAMA_COUNTRY_CODE + "00";

        var numericalRearrangedAccountNumber = new StringBuilder();

        for (int i = 0; i < rearrangedAccountNumber.length(); i++) {
            if (Character.isDigit(rearrangedAccountNumber.charAt(i))) {
                numericalRearrangedAccountNumber.append(rearrangedAccountNumber.charAt(i));
            } else {
                numericalRearrangedAccountNumber.append(convertCharToNumber(rearrangedAccountNumber.charAt(i)));
            }
        }

        BigInteger modResult = new BigInteger(numericalRearrangedAccountNumber.toString()).mod(BigInteger.valueOf(97));
        BigInteger checkSum = BigInteger.valueOf(98).subtract(modResult);

        return checkSum.compareTo(BigInteger.valueOf(10)) >= 0 ? checkSum.toString() : "0" + checkSum;

    }

    /**
     * Converts an IBAN to its numerical representation for use in the IBAN verification algorithm
     *
     * @param iban the IBAN to convert
     * @return the numberical representation of the IBAN
     */
    private static String ibanToNumericalIban(String iban) {
        String rearrangedIban = iban.substring(4) + iban.substring(0, 4);

        var numericalIbanRepresentation = new StringBuilder();

        for (char c : rearrangedIban.toCharArray()) {
            if (Character.isDigit(c)) {
                numericalIbanRepresentation.append(c);
            } else {
                numericalIbanRepresentation.append(convertCharToNumber(c));
            }
        }

        return numericalIbanRepresentation.toString();
    }

    /**
     * Converts a character to the numerical representation as for the IBAN verification
     *
     * @param character the character to convert
     * @return the numerical representation
     */
    private static String convertCharToNumber(char character) {
        char[] capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        int i = 0;
        for (; i < capitals.length; i++) {
            if (character == capitals[i]) {
                break;
            }
        }
        return String.valueOf(i + 10);
    }

    /**
     * Helper method to check if a string contains non-alphanumeric characters
     *
     * @param s the string to check
     * @return true if the string contains non-alphanumeric characters, false otherwise
     */
    private static boolean containsNonAlphanumericCharacters(String s) {
        Pattern nonAlphanumeric = Pattern.compile("[^a-zA-Z\\d]");
        return nonAlphanumeric.matcher(s).find();
    }

    /**
     * Converts an internal bank account number to its IBAN
     *
     * @param internalBankAccountNumber the internal bank account number
     * @return the IBAN
     * @throws IllegalArgumentException if internalBankAccount number is null, of incorrect length, or contains
     *                                  non-alphanumeric characters
     */
    public static String internalBankAccountNumberToIban(String internalBankAccountNumber) throws
                                                                                           IllegalArgumentException {
        if (internalBankAccountNumber == null) {
            throw new IllegalArgumentException("Account number cannot be null");
        }

        internalBankAccountNumber = internalBankAccountNumber.replace(" ", "");

        if (internalBankAccountNumber.length() != LLAMA_BBAN_FORMAT.accountNumberLength()) {
            throw new IllegalArgumentException("Incorrect bank account number format");
        }

        if (containsNonAlphanumericCharacters(internalBankAccountNumber)) {
            throw new IllegalArgumentException("Account number can only contain alphanumeric characters");
        }

        return LLAMA_COUNTRY_CODE
               + calculateChecksum(internalBankAccountNumber)
               + llamaBankCode
               + internalBankAccountNumber;
    }

    public static String ibanToInternalBankAccountNumber(String iban) throws IllegalArgumentException {
        if (!isValidInternalIban(iban)) {
            throw new IllegalArgumentException("Not an internal IBAN number");
        }
        return iban.substring(8);
    }
}
