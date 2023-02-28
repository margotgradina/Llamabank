package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.BankAccountDTO;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

class BankAccountControllerTest {
    private final static BankAccountDTO bankAccountDTO1 = mock(BankAccountDTO.class);
    private final static BankAccountDTO bankAccountDTO2 = mock(BankAccountDTO.class);


    private final static List<BankAccountDTO> VALID_BANK_ACCOUNT_DTO_LIST = new ArrayList<>(List.of(bankAccountDTO1, bankAccountDTO2));

    private final static ResponseEntity<List<BankAccountDTO>> VALID_BANK_ACCOUNT_RESPONSE_ENTITY = new ResponseEntity<>(VALID_BANK_ACCOUNT_DTO_LIST, HttpStatus.OK);
    private final static ResponseEntity<Object> VALID_NO_CONTENT_RESPONSE_ENTITY = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    /**
     * @verifies return response entity of list of bank accounts and httpStatus ok if present
     * @see BankAccountController getBankAccountsListResponseEntity(java.util.List)
     */
    @Test
    public void getBankAccountsListResponseEntity_shouldReturnResponseEntityOfListOfBankAccountsAndHttpStatusOkIfPresent() throws Exception {
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        ClientRepository clientRepository = mock(ClientRepository.class);
        SmallBusinessBankAccountRepository smallBusinessBankAccountRepository = mock(SmallBusinessBankAccountRepository.class);
        PrivateBankAccountRepository privateBankAccountRepository = mock(PrivateBankAccountRepository.class);
        AccountManagerRepository accountManagerRepository = mock(AccountManagerRepository.class);


        BankAccountController bankAccountController = new BankAccountController(bankAccountRepository, clientRepository, smallBusinessBankAccountRepository, privateBankAccountRepository, accountManagerRepository);
        List<SmallBusinessBankAccount> givenList = new ArrayList<>();

        SmallBusinessBankAccount smallBusinessBankAccount1 = mock(SmallBusinessBankAccount.class);
        SmallBusinessBankAccount smallBusinessBankAccount2 = mock(SmallBusinessBankAccount.class);


        givenList.add(0, smallBusinessBankAccount1);
        givenList.add(1, smallBusinessBankAccount2);
        Assertions.assertNotNull(givenList);
        Assertions.assertEquals(200, VALID_BANK_ACCOUNT_RESPONSE_ENTITY.getStatusCodeValue());
    }

    /**
     * @verifies return httpStatus no_content status is no bank accounts are present
     * @see BankAccountController getBankAccountsListResponseEntity(java.util.List)
     */
    @Test
    public void getBankAccountsListResponseEntity_shouldReturnHttpStatusNo_contentStatusIsNoBankAccountsArePresent() throws Exception {

        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        ClientRepository clientRepository = mock(ClientRepository.class);
        SmallBusinessBankAccountRepository smallBusinessBankAccountRepository = mock(SmallBusinessBankAccountRepository.class);
        PrivateBankAccountRepository privateBankAccountRepository = mock(PrivateBankAccountRepository.class);
        AccountManagerRepository accountManagerRepository = mock(AccountManagerRepository.class);


        BankAccountController bankAccountController = new BankAccountController(bankAccountRepository, clientRepository, smallBusinessBankAccountRepository, privateBankAccountRepository, accountManagerRepository);
        List<SmallBusinessBankAccount> givenList = new ArrayList<>();

        Assertions.assertEquals(VALID_NO_CONTENT_RESPONSE_ENTITY, bankAccountController.getBankAccountsListResponseEntity(givenList));
        Assertions.assertEquals(VALID_NO_CONTENT_RESPONSE_ENTITY, bankAccountController.getBankAccountsListResponseEntity(givenList));

    }
}
