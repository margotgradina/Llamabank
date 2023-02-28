package com.dellama.bank.webapi.model.iban;

/**
 * Record representing a naive BBAN format (i.e. bank code includes the branch code if applicable)
 * @param bankCodeLength length of the bank code including branch code
 * @param accountNumberLength length of the internal bank account number
 */
public record BbanFormat(int bankCodeLength, int accountNumberLength) {
}
