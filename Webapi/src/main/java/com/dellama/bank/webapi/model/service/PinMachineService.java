package com.dellama.bank.webapi.model.service;

import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.PinMachine;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.model.approval.PinMachineApproval;
import com.dellama.bank.webapi.repository.ApprovalRepository;
import com.dellama.bank.webapi.repository.PinMachineRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PinMachineService {

    private final PinMachineRepository pinMachineRepository;
    private final ApprovalRepository approvalRepository;

    public PinMachineService(PinMachineRepository pinMachineRepository, ApprovalRepository approvalRepository) {
        this.pinMachineRepository = pinMachineRepository;
        this.approvalRepository = approvalRepository;
    }

    public PinMachineRepository getPinMachineRepository() {
        return pinMachineRepository;
    }

    public void save(PinMachine pinMachine) {
        pinMachineRepository.save(pinMachine);
    }


    public boolean isConnectionCodeAndBankAccountInDataBase(int connectionCode, BankAccount bankAccount) {
        PinMachine pinMachine = new PinMachine();
        pinMachine.setConnectionCode(connectionCode);
        pinMachine.setSmallBusinessBankAccount((SmallBusinessBankAccount) bankAccount);
        return pinMachineRepository.exists(Example.of(pinMachine));
    }

    public boolean isIdentificationCodeIsInDataBase(int identificationcode) {
        PinMachine pinMachine = new PinMachine();
        pinMachine.setIdentificationNumberPinMachine(identificationcode);
        return pinMachineRepository.exists(Example.of(pinMachine));
    }


    public Optional<PinMachine> findPinMachineByConnectionCodeAndBankAccount(int connectionCode, SmallBusinessBankAccount smallBusinessBankAccount) {
        return pinMachineRepository.findPinMachineByConnectionCodeAndSmallBusinessBankAccount(connectionCode, smallBusinessBankAccount);
    }

    public Optional<PinMachineApproval> findPinMachineApprovalBySmallBusinessBankAccount(SmallBusinessBankAccount smallBusinessBankAccount) {
        return approvalRepository.findPinMachineApprovalBySmallBusinessBankAccount(smallBusinessBankAccount.getId());
    }
}


