package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.*;
import com.dellama.bank.webapi.model.AccountManager;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.Client;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.model.approval.*;
import com.dellama.bank.webapi.model.service.ApprovalService;
import com.dellama.bank.webapi.model.service.BankAccountService;
import com.dellama.bank.webapi.model.service.UserAccountService;
import com.dellama.bank.webapi.repository.AccountManagerRepository;
import com.dellama.bank.webapi.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The ApprovalController class provides a REST controller that handles
 * HTTP requests for the approvals in the Llama Bank banking system
 */

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {
    private final ApprovalService approvalService;
    private final BankAccountService bankAccountService;
    private final UserAccountService userAccountService;
    private final AccountManagerRepository accountManagerRepository;

    public ApprovalController(ApprovalService approvalService,
                              ClientRepository clientRepository,
                              BankAccountService bankAccountService,
                              UserAccountService userAccountService,
                              AccountManagerRepository accountManagerRepository) {
        this.approvalService = approvalService;
        this.bankAccountService = bankAccountService;
        this.userAccountService = userAccountService;
        this.accountManagerRepository = accountManagerRepository;
    }

    @GetMapping
    public List<Approval> getApprovals() {
        return approvalService.findAll();
    }

    @GetMapping("/pending")
    public List<ApprovalInfoDTO> getPendingApprovals(@RequestParam long accountManagerId) {

       Optional<AccountManager> accountManagerFromDatabase = accountManagerRepository.findById(accountManagerId);

       if (accountManagerFromDatabase.isEmpty()) {
           return new ArrayList<>();
       }

        boolean smallBusinessApprovals = accountManagerFromDatabase.get().isSmallBusinessManager();

        return (smallBusinessApprovals
                ? approvalService.findApprovalsForSmallBusinessAccountManager()
                : approvalService.findApprovalsForPrivateAccountManager()).stream()
                                                                          .map(ApprovalInfoDTO::approvalInfoDTOFromApproval)
                                                                          .collect(
                                                                                  Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Approval> findById(@PathVariable Long id) {
        return approvalService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Sets the status of an approval
     *
     * @param approvalStatusDTO DTO containing the id of the approval and the ApprovalStatus to set the approval to
     * @return ApprovalStatusDTO with the new ApprovalStatus
     */
    @PostMapping("/setapprovalstatus")
    public ResponseEntity<ApprovalStatusDTO> setApprovalStatus(@RequestBody ApprovalStatusDTO approvalStatusDTO) {
        if (approvalStatusDTO.getApprovalId() == null || approvalStatusDTO.getApprovalStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Approval> requestFromDatabase = approvalService.findById(approvalStatusDTO.getApprovalId());

        if (requestFromDatabase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Approval currentApproval = requestFromDatabase.get();

        if (currentApproval.getApprovalStatus().equals(ApprovalStatus.APPROVED) || currentApproval.getApprovalStatus()
                                                                                                  .equals(ApprovalStatus.DENIED)) {
            return ResponseEntity.unprocessableEntity().build();
        }

        boolean result = switch (approvalStatusDTO.getApprovalStatus()) {
            case APPROVED -> approvalService.approve(currentApproval);
            case DENIED -> approvalService.deny(currentApproval);
            default -> false;
        };

        currentApproval = approvalService.save(currentApproval);

        return result
               ? ResponseEntity.ok(new ApprovalStatusDTO(currentApproval.getId(),
                                                         currentApproval.getApprovalStatus()))
               : ResponseEntity.internalServerError().build();
    }

    /**
     * Creates a new AddAccountHolderApproval in the system
     *
     * @param addAccountHolderApprovalDTO DTO containing the information necessary for creating the request
     * @return AddAccountHolderApprovalDTO updated with the approvalId
     */
    @PostMapping("/new/addaccountholderapproval")
    public ResponseEntity<AddAccountHolderApprovalDTO> newAddAccountHolderApproval(@RequestBody AddAccountHolderApprovalDTO addAccountHolderApprovalDTO) {
        Optional<Client> client = userAccountService.findClientById(addAccountHolderApprovalDTO.getClientId());
        Optional<Client> newAccountHolder =
                userAccountService.findClientById(addAccountHolderApprovalDTO.getNewAccountHolderId());
        Optional<BankAccount> bankAccountFromDatabase = bankAccountService.findBankAccountByAccountNumber(
                addAccountHolderApprovalDTO.getBankAccountNumber());

        if (client.isEmpty() || newAccountHolder.isEmpty() || bankAccountFromDatabase.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        BankAccount bankAccount = bankAccountFromDatabase.get();

        if (!bankAccount.getAccountHolders().contains(client.get()) || client.get()
                                                                             .getId()
                                                                             .equals(newAccountHolder.get().getId())) {
            return ResponseEntity.unprocessableEntity().build();
        }


        Optional<AddAccountHolderApproval> approvalFromDBByBankAccountAndSecCode =
                approvalService.findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(
                        bankAccount.getId(),
                        addAccountHolderApprovalDTO.getSecurityCode());
        Optional<AddAccountHolderApproval> approvalFromDBByBankAccountAndNewAccountHolder =
                approvalService.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(
                        bankAccount.getId(),
                        newAccountHolder.get()
                                        .getId());

        if (approvalFromDBByBankAccountAndSecCode.isPresent()
            || approvalFromDBByBankAccountAndNewAccountHolder.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


        var addAccountHolderApproval = new AddAccountHolderApproval(client.get(),
                                                                    newAccountHolder.get(),
                                                                    bankAccountFromDatabase.get(),
                                                                    addAccountHolderApprovalDTO.getSecurityCode());

        addAccountHolderApproval = (AddAccountHolderApproval) approvalService.save(addAccountHolderApproval);

        return ResponseEntity.ok(new AddAccountHolderApprovalDTO(addAccountHolderApproval.getId(),
                                                                 addAccountHolderApproval.getClient().getId(),
                                                                 addAccountHolderApproval.getNewAccountHolder().getId(),
                                                                 addAccountHolderApproval.getBankAccount()
                                                                                         .getAccountNumber(),
                                                                 addAccountHolderApproval.getSecurityCode(),
                                                                 addAccountHolderApproval.getApprovalStatus()));
    }

    /**
     * Creates a new PinMachineApproval
     *
     * @param pinMachineApprovalDTO DTO containing the info necessary for creating a PinMachineApproval
     * @return PinMachineApprovalDTO updated with the approvalId
     */
    @PostMapping("/new/pinmachineapproval")
    public ResponseEntity<PinMachineApprovalDTO> newPinMachineApproval(@RequestBody PinMachineApprovalDTO pinMachineApprovalDTO) {
        Optional<Client> client = userAccountService.findClientById(pinMachineApprovalDTO.getClientId());

        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<BankAccount> linkedBankAccount = bankAccountService.findBankAccountByAccountNumber(
                pinMachineApprovalDTO.getLinkedBankAccountNumber());
        if (linkedBankAccount.isEmpty() || !(linkedBankAccount.get() instanceof SmallBusinessBankAccount)) {
            return ResponseEntity.unprocessableEntity().build();
        }

        var newPinMachineApproval = new PinMachineApproval(client.get(),
                                                           (SmallBusinessBankAccount) linkedBankAccount.get());

        newPinMachineApproval = (PinMachineApproval) approvalService.save(newPinMachineApproval);

        return ResponseEntity.ok(new PinMachineApprovalDTO(newPinMachineApproval.getId(),
                                                           newPinMachineApproval.getClient().getId(),
                                                           newPinMachineApproval.getLinkedBankAccount()
                                                                                .getAccountNumber(),
                                                           newPinMachineApproval.getApprovalStatus()));
    }

    /**
     * Creates a new OpenBankAccountApproval
     *
     * @param openBankAccountApprovalDTO DTO containing the info necessary for creating a new OpenBankAccountApproval
     * @return OpenBankAccountApprovalDTO updated with the approval Id
     */
    @PostMapping("/new/openbankaccountapproval")
    public ResponseEntity<OpenBankAccountApprovalDTO> newOpenBankAccountApproval(@RequestBody OpenBankAccountApprovalDTO openBankAccountApprovalDTO) {
        Optional<Client> client = userAccountService.findClientById(openBankAccountApprovalDTO.getClientId());
        if (client.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        OpenBankAccountApproval approval;
        if (openBankAccountApprovalDTO.getSector() != null) {
            approval = new OpenBankAccountApproval(client.get(),
                                                   OpenBankAccountType.BUSINESS,
                                                   openBankAccountApprovalDTO.getSector());
        } else {
            approval = new OpenBankAccountApproval(client.get(), OpenBankAccountType.PRIVATE, null);
        }

        approval = (OpenBankAccountApproval) approvalService.save(approval);

        return ResponseEntity.ok(new OpenBankAccountApprovalDTO(approval.getId(),
                                                                approval.getClient().getId(),
                                                                approval.getSmallBusinessSector(),
                                                                approval.getApprovalStatus()));
    }

    /**
     * Validates the request of a newAccountHolder to be added to an existing bankaccount
     *
     * @param addAccountHolderApprovalDTO
     * @return Information on whether the account was succesfully added
     */
    @PostMapping("/checkaccountholderapproval")
    public ResponseEntity<String> CheckAccountHolderApproval(@RequestBody AddAccountHolderApprovalDTO addAccountHolderApprovalDTO) {
        String bankAccountNumber = addAccountHolderApprovalDTO.getBankAccountNumber();
        String securityCode = addAccountHolderApprovalDTO.getSecurityCode();
        Long newAccountHolderId = addAccountHolderApprovalDTO.getNewAccountHolderId();
        BankAccount bankAccount;
        Long bankAccountId;
        Client accountHolder;
        AddAccountHolderApproval approval;

        //check if newAccountHolder is in the system
        Optional<Client> newAccountHolder = userAccountService.findClientById(newAccountHolderId);
        if (!newAccountHolder.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NewAccountHolder not found in Database");
        } else {
            accountHolder = newAccountHolder.get();
        }

        //check if bankaccount is in the system
        Optional<BankAccount> foundBankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNumber);
        if (!foundBankAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bankaccount number not found in Database");
        } else {
            bankAccount = foundBankAccount.get();
            bankAccountId = bankAccount.getId();
        }

        //get the approval from DB based on bankaccountId and accountholder id, check if approval exists
        Optional<AddAccountHolderApproval> foundApproval =
                approvalService.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(
                        bankAccountId,
                        newAccountHolderId);

        if (!foundApproval.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Approval not found");
        } else {
            approval = foundApproval.get();
        }

        //checks if approval has already been used
        if (approval.isConsumed()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Approval has already been consumed");
        }

        //check if securitycode is correct
        if (!approval.getSecurityCode().equals(securityCode)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Wrong securitycode");
        }

        //check if approval status is approved, else return "application not yet approved";
        ApprovalStatus approvalStatus = approval.getApprovalStatus();
        if (approvalStatus.equals(ApprovalStatus.PENDING)) {
            return ResponseEntity.status(HttpStatus.OK).body("approval is pending, cannot be added yet");
        } else if (approvalStatus.equals(ApprovalStatus.DENIED)) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("approval is denied, cannot be added. Contact your accountmanager");
        } else { //approval is approved
            bankAccountService.addBankAccountToAccountHolder(accountHolder, bankAccount);
            userAccountService.saveClient(accountHolder);
            bankAccountService.save(bankAccount);
            approval.setConsumed(true);
            approvalService.save(approval);
            return ResponseEntity.status(HttpStatus.OK).body("Bankaccount is successfully added to your account");
        }
    }

    @CrossOrigin
    @GetMapping("/count/{bankAccountNr}")
    public ResponseEntity<List<ApprovalStatusCountDTO>> countPendingAndApprovedPinTerminals(@PathVariable String bankAccountNr) {
        List<ApprovalStatusCountDTO> foundList = approvalService.countPendingAndApprovedPinTerminals(bankAccountNr);

        if (foundList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(foundList, HttpStatus.OK);
        }
    }
}
