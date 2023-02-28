package com.dellama.bank.pinterminal.model.service;

import com.dellama.bank.pinterminal.dto.TransactionDTO;
import com.dellama.bank.pinterminal.model.PinMachine;

import java.time.LocalDateTime;

public interface PersistencyService {

    boolean addTransactionToJournal(TransactionDTO transactionDTO, LocalDateTime localDateTime);

    boolean createJournalPage(LocalDateTime localDateTime);

    boolean savePinMachineDetails(PinMachine pinMachine);

    boolean loadPinMachineDetails();
}
