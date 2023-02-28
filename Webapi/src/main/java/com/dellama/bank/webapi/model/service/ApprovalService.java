package com.dellama.bank.webapi.model.service;

import com.dellama.bank.webapi.DTO.ApprovalStatusCountDTO;
import com.dellama.bank.webapi.model.PinMachine;
import com.dellama.bank.webapi.model.approval.*;
import com.dellama.bank.webapi.repository.ApprovalRepository;
import com.dellama.bank.webapi.repository.ClientRepository;
import com.dellama.bank.webapi.repository.PinMachineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {
    private final ApprovalRepository approvalRepository;
    private final BankAccountService bankAccountService;
    private final PinMachineRepository pinMachineRepository;
    private final ClientRepository clientRepository;

    public ApprovalService(ApprovalRepository approvalRepository,
                           BankAccountService bankAccountService,
                           PinMachineRepository pinMachineRepository, ClientRepository clientRepository) {
        this.approvalRepository = approvalRepository;
        this.bankAccountService = bankAccountService;
        this.pinMachineRepository = pinMachineRepository;
        this.clientRepository = clientRepository;
    }

    public boolean approve(Approval approval) {

        if (approval instanceof OpenBankAccountApproval openBankAccountApproval) {
            approveOpenBankAccountApproval(openBankAccountApproval);
        } else if (approval instanceof PinMachineApproval pinMachineApproval) {
            approvePinMachineApproval(pinMachineApproval);
        }
        approval.setApprovalStatus(ApprovalStatus.APPROVED);
        approvalRepository.save(approval);
        return true;
    }

    private void approvePinMachineApproval(PinMachineApproval pinMachineApproval) {
        var pinMachine = new PinMachine(pinMachineApproval.getLinkedBankAccount());
        pinMachineRepository.save(pinMachine);
        pinMachineApproval.setPinMachine(pinMachine);
    }

    private void approveOpenBankAccountApproval(OpenBankAccountApproval openBankAccountApproval) {
        var bankAccount = switch (openBankAccountApproval.getOpenBankAccountType()) {
            case PRIVATE -> bankAccountService.createPrivateBankAccount(openBankAccountApproval.getClient());
            case BUSINESS -> bankAccountService.createSmallBusinessBankAccount(openBankAccountApproval.getClient(),
                                                                               openBankAccountApproval.getSmallBusinessSector());
        };
        openBankAccountApproval.getClient().addBankAccount(bankAccount);
        bankAccountService.save(bankAccount);
        clientRepository.save(openBankAccountApproval.getClient());
        openBankAccountApproval.setBankAccountNumber(bankAccount.getAccountNumber());
        openBankAccountApproval.setConsumed(true);
    }

    public boolean deny(Approval approval) {
        approval.setApprovalStatus(ApprovalStatus.DENIED);
        approvalRepository.save(approval);
        return true;
    }

    public List<Approval> findAll() {
        return approvalRepository.findAll();
    }

    public Optional<Approval> findById(Long id) {
        return approvalRepository.findById(id);
    }

    public Approval save(Approval approval) {
        return approvalRepository.save(approval);
    }

    public List<Approval> findApprovalsForPrivateAccountManager() {
        return approvalRepository.findApprovalsForPrivateAccountManager();
    }

    public List<Approval> findApprovalsForSmallBusinessAccountManager() {
        return approvalRepository.findApprovalsForSmallBusinessAccountManager();
    }

    public Optional<AddAccountHolderApproval> findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(Long bankAccountId, String securityCode){
     return approvalRepository.findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(bankAccountId, securityCode);
    }

    public Optional<AddAccountHolderApproval> findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(Long bankAccountId, Long newAccountHolderId){
        return approvalRepository.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(bankAccountId, newAccountHolderId);
    }

    public List<ApprovalStatusCountDTO> countPendingAndApprovedPinTerminals(String accountNumber){
        return approvalRepository.countPendingAndApprovedPinTerminals(accountNumber);
    }

}
