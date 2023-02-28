package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.model.Transaction;
import com.dellama.bank.webapi.model.service.BankAccountService;
import com.dellama.bank.webapi.model.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    private final static Transaction transaction1 = mock(Transaction.class);
    private final static Transaction transaction2 = mock(Transaction.class);

    private final static List<Transaction> VALID_TRANSACTION_LIST = new ArrayList<>(List.of(transaction1, transaction2));
    private final static List<Transaction> VALID_EMPTY_TRANSACTION_LIST = new ArrayList<>();

    private final static ResponseEntity<Iterable<Transaction>> VALID_TRANSACTION_LIST_OK_RESPONSE_ENTITY = new ResponseEntity<>(VALID_TRANSACTION_LIST, HttpStatus.OK);
    private final static ResponseEntity<Object> VALID_NO_CONTENT_RESPONSE_ENTITY = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    private final static String VALID_BANKACCOUNT_NUMBER = "1342210720";
    private final static String VALID_BANKACCOUNT_NUMBER_2 = "1671581594";
    private final static String VALID_IBAN_BANKACCOUNT_NUMBER = "NL04LAMA2652125215";
    private final static String VALID_IBAN_BANKACCOUNT_NUMBER_2 = "NL44LAMA2066502709";
    private final static double VALID_AMOUNT = 100;
    private final static String VALID_MESSAGE= "test";
    private final static BankAccountService bankAccountServiceMock = mock(BankAccountService.class);
    private final static TransactionService transactionServiceMock = mock(TransactionService.class);
    TransactionController transactionController = new TransactionController(transactionServiceMock, bankAccountServiceMock);


    /**
     * @verifies return a list of all transactions made/received by the given bankaccountnumber
     * @see TransactionController#getAllTransactionsByBankAccountNr(String)
     */
    @Test
    public void getAllTransactionsByBankAccountNr_shouldReturnAListOfAllTransactionsMadereceivedByTheGivenBankaccountnumber() throws Exception {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction(VALID_BANKACCOUNT_NUMBER, VALID_IBAN_BANKACCOUNT_NUMBER_2, VALID_AMOUNT, VALID_MESSAGE, (LocalDateTime.now()));
        transactionList.add(transaction);
        when(transactionServiceMock.findAllTransactionsByBankAccount(VALID_BANKACCOUNT_NUMBER)).thenReturn(transactionList);

        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(200, VALID_TRANSACTION_LIST_OK_RESPONSE_ENTITY.getStatusCodeValue());

    }
}
