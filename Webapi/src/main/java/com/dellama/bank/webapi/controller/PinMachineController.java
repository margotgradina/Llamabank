package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.PinMachineCheckConnectionDTO;
import com.dellama.bank.webapi.DTO.PinMachineConnectionDTO;
import com.dellama.bank.webapi.DTO.TransactionDTO;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.PinMachine;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.model.approval.PinMachineApproval;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.model.service.BankAccountService;
import com.dellama.bank.webapi.model.service.PinMachineService;
import com.dellama.bank.webapi.model.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

/**
 * The PinMachineController class provides a REST controller that handles
 * HTTP requests coming from pinmachines in the Llama Bank banking system
 */
@RestController
@RequestMapping("/api/pinmachine")
public class PinMachineController {

    private static final int FIVE_DIGIT_NUMBER = 99999;
    private static final int EIGHT_DIGIT_NUMBER = 99999999;
    private final TransactionService transactionService;
    private final TransactionController transactionController;
    private final PinMachineService pinMachineService;
    private final BankAccountService bankAccountService;
    private PinMachine pinMachine = new PinMachine();


    public PinMachineController(TransactionService transactionService, TransactionController transactionController, PinMachineService pinMachineService, BankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.transactionController = transactionController;
        this.pinMachineService = pinMachineService;
        this.bankAccountService = bankAccountService;
    }

    /**
     * Endpoint for pinmachine to make transactions
     *
     * @param transactionDTO
     * @return statuscode of transaction
     */
    @PostMapping(value = "/maketransaction")
    public ResponseEntity<String> makeTransactionViaPin(@RequestBody TransactionDTO transactionDTO) {
        return transactionController.addTransaction(transactionDTO);
    }


    @GetMapping(value = "/requestConnectionCode")
    public ResponseEntity<PinMachineConnectionDTO> connectPinMachine(@RequestParam String bankAccountNr) {

        //find bank account
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNr);

        if (bankAccount.isPresent() ) {
            String bankAccountIBAN = IbanUtils.internalBankAccountNumberToIban(bankAccountNr);
            //generate number
            int connectionCode = getGeneratedNumber(FIVE_DIGIT_NUMBER);

            Long bankAccountId = bankAccount.get().getId();

            //save json (to send to front end)
            PinMachineConnectionDTO pinMachineConnectionDTO = new PinMachineConnectionDTO(bankAccountIBAN, connectionCode);

            Optional<SmallBusinessBankAccount> smallBusinessBankAccountOptional = bankAccountService.findSmallBusinessBankAccountById(bankAccountId);
            if ( smallBusinessBankAccountOptional.isPresent() ) {
                SmallBusinessBankAccount smallBusinessBankAccount = smallBusinessBankAccountOptional.get();
                Optional<PinMachineApproval> pinMachineApprovalOptional = pinMachineService.findPinMachineApprovalBySmallBusinessBankAccount(smallBusinessBankAccount);
                if ( pinMachineApprovalOptional.isPresent() ) {
                    //save pinmachine with 5-digit code to database
                    pinMachine.setConnectionCode(connectionCode);
                    pinMachine.setSmallBusinessBankAccount(smallBusinessBankAccount);
                    pinMachineService.save(pinMachine);
                    return new ResponseEntity<>(pinMachineConnectionDTO, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }}

    /**
     * @param boundLength
     * @return
     */
    private int getGeneratedNumber(int boundLength) {
        Random random = new Random();
        return random.nextInt(boundLength);
    }

    /**
     * @param pinMachineConnectionDTO request body
     * @return //TODO write some tests??? simplyfy code???
     */
    @PostMapping("checkConnection")
    public ResponseEntity<PinMachineCheckConnectionDTO> checkConnection(@RequestBody PinMachineConnectionDTO pinMachineConnectionDTO) {
        String bankAccountNr;
        Long bankAccountId;
        String iban = pinMachineConnectionDTO.getIban();
        Optional<BankAccount> bankAccount;
        //ibad valid format check


        if ( IbanUtils.isValidInternalIban(iban) ) {
            bankAccountNr = IbanUtils.ibanToInternalBankAccountNumber(iban);
            bankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNr);
            SmallBusinessBankAccount smallBusinessBankAccount = ((SmallBusinessBankAccount) bankAccount.get());
            if ( bankAccount.isPresent() ) {
                bankAccountId = bankAccount.get().getId();
                int connectionCode = pinMachineConnectionDTO.getConnectionCode();
                Optional<PinMachine> foundPinMachine = pinMachineService.findPinMachineByConnectionCodeAndBankAccount(connectionCode, smallBusinessBankAccount);
                if ( foundPinMachine.isPresent() ) {
                    PinMachine pinMachine = foundPinMachine.get();
                    boolean isConnectionCodeAndBankAccountIdInDatabase = pinMachineService.isConnectionCodeAndBankAccountInDataBase(connectionCode, bankAccount.get());
                    if ( isConnectionCodeAndBankAccountIdInDatabase ) {
                        int identificationCode = generateAndCheckIdentificationCode();
                        pinMachine.setIdentificationNumberPinMachine(identificationCode);
                        pinMachineService.save(pinMachine);

                        return new ResponseEntity<>(new PinMachineCheckConnectionDTO("🦙 connection approved 🦙", true, identificationCode), HttpStatus.OK);

                    }
                }
            }

        }
        return badRequestResponseEntity();
    }

    private int generateAndCheckIdentificationCode() {
        int identificationCode = 0;
        boolean isUnique = false;
        while (!isUnique) {
            identificationCode = getGeneratedNumber(EIGHT_DIGIT_NUMBER);
            boolean isIdentificationCodeInDataBase = pinMachineService.isIdentificationCodeIsInDataBase(identificationCode);
            if ( !isIdentificationCodeInDataBase ) {
                isUnique = true;
            }
        }
        return identificationCode;

    }


    public ResponseEntity<PinMachineCheckConnectionDTO> badRequestResponseEntity() {
        return new ResponseEntity<>(new PinMachineCheckConnectionDTO("😪 llama not found...check iban and/or connection code", false), HttpStatus.BAD_REQUEST);
    }


}





