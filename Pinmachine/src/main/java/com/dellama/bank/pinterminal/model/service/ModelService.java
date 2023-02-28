package com.dellama.bank.pinterminal.model.service;

import com.dellama.bank.pinterminal.dto.ConnectionDTO;
import com.dellama.bank.pinterminal.dto.ConnectionResponseDTO;
import com.dellama.bank.pinterminal.dto.TransactionDTO;
import com.dellama.bank.pinterminal.model.PinMachine;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ModelService {
    ConnectionResponseDTO checkConnection(ConnectionDTO connectionDTO) throws IOException, InterruptedException;

     String makeTransaction(TransactionDTO transactionDTO) throws IOException;

    boolean createJournalPage(LocalDateTime localDateTime);

    boolean savePinMachineDetails(PinMachine pinMachine);

    boolean loadPinMachineDetails();
}
