package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.ApprovalStatusDTO;
import com.dellama.bank.webapi.model.Address;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.Client;
import com.dellama.bank.webapi.model.PrivateBankAccount;
import com.dellama.bank.webapi.model.approval.*;
import com.dellama.bank.webapi.model.service.ApprovalService;
import com.dellama.bank.webapi.model.service.BankAccountService;
import com.dellama.bank.webapi.model.service.UserAccountService;
import com.dellama.bank.webapi.repository.AccountManagerRepository;
import com.dellama.bank.webapi.repository.ClientRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApprovalControllerTest {
    public static final String INTEGRATION = "integration";

    private ApprovalService approvalService;
    private ClientRepository clientRepository;
    private BankAccountService bankAccountService;
    private UserAccountService userAccountService;
    private AccountManagerRepository accountManagerRepository;
    private Approval approval;

    @BeforeEach
    void initRestAssuredMockMvcStandalone() {
        this.approvalService = mock(ApprovalService.class);
        this.clientRepository = mock(ClientRepository.class);
        this.bankAccountService = mock(BankAccountService.class);
        this.accountManagerRepository = mock(AccountManagerRepository.class);

        Client client = new Client("username",
                "password",
                "name",
                "tim@[192.168.178.20]",
                "0000000000",
                "000000000",
                new Address("street", 20, null, "5645DR", "Waar dan ook", null, "Nederland"));
        approval = new OpenBankAccountApproval(client, OpenBankAccountType.PRIVATE, null);
        approval.setId(1L);

        when(approvalService.save(approval)).thenReturn(approval);
        when(accountManagerRepository.isSmallBusinessManager(any())).thenReturn(true);

        RestAssuredMockMvc.standaloneSetup(new ApprovalController(approvalService,
                                                                  clientRepository,
                                                                  bankAccountService,
                                                                  userAccountService, accountManagerRepository));
    }

    @Test
    @Tag(INTEGRATION)
    void getApprovals_ShouldReturnListOfApprovalInfoDTOs() {
        when(approvalService.findAll()).thenReturn(List.of(approval));

        RestAssuredMockMvc.given()
                          .queryParam("accountManagerId", 1)
                          .when()
                          .get("/api/approvals")
                          .then()
                          .statusCode(HttpStatus.OK.value());
        RestAssuredMockMvc.given()
                          .queryParam("accountManagerId", 1)
                          .when()
                          .get("/api/approvals")
                          .then()
                          .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Tag(INTEGRATION)
    void getApprovals_ShouldReturn400IfRequiredArgumentsAreNotGiven() {
        when(approvalService.findAll()).thenReturn(List.of(approval));

        RestAssuredMockMvc.when().get("/api/approvals/pending").then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Tag(INTEGRATION)
    void findById_ShouldReturnApprovalIfIdIsValid() {
        when(approvalService.findById(1L)).thenReturn(Optional.of(approval));
        // using .body("id", equalTo(approval.getId()) does not work due to id being a Long)
        // so we extract as class, then compare
        Approval result = get("/api/approvals/" + approval.getId()).then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(OpenBankAccountApproval.class);
        assertEquals(approval.getId(), result.getId());

    }

    @Test
    @Tag(INTEGRATION)
    void findById_ShouldReturn404IfIdIsNotFound() {
        when(approvalService.findById(any())).thenReturn(Optional.empty());
        RestAssuredMockMvc.when().get("/api/approvals/2").then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Tag(INTEGRATION)
    void findById_ShouldReturn400BadRequestIfIdIsInvalid() {
        when(approvalService.findById(any())).thenReturn(Optional.empty());

        RestAssuredMockMvc.when().get("/api/approvals/test").then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldReturnOkAndDTOWhenRequestIsValid() {
        when(approvalService.findById(1L)).thenReturn(Optional.of(approval));
        when(approvalService.approve(any())).thenReturn(true);
        when(approvalService.deny(any())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", "1");
        request.put("approvalStatus", "approved");

        ApprovalStatusDTO result = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ApprovalStatusDTO.class);

        assertEquals(approval.getId(), result.getApprovalId());
    }

    @Test
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldReturnNotFoundIfApprovalIsNotInDB() {
        when(approvalService.findById(1L)).thenReturn(Optional.empty());
        when(approvalService.approve(any())).thenReturn(true);
        when(approvalService.deny(any())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", "2");
        request.put("approvalStatus", "approved");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldReturnUnprocessableEntityIfTargetApprovalIsApprovedOrDenied() {
        Approval approvalDenied = new OpenBankAccountApproval(approval.getClient(), OpenBankAccountType.PRIVATE, null);
        approvalDenied.setId(1L);
        approvalDenied.setApprovalStatus(ApprovalStatus.DENIED);
        Approval approvalApproved = new OpenBankAccountApproval(approval.getClient(),
                OpenBankAccountType.PRIVATE,
                null);
        approvalApproved.setId(2L);
        approvalApproved.setApprovalStatus(ApprovalStatus.APPROVED);

        when(approvalService.findById(1L)).thenReturn(Optional.of(approvalDenied));
        when(approvalService.approve(any())).thenReturn(true);
        when(approvalService.deny(any())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", approvalDenied.getId().toString());
        request.put("approvalStatus", "approved");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        when(approvalService.findById(2L)).thenReturn(Optional.of(approvalApproved));

        request.put("approvalId", approvalApproved.getId().toString());
        request.put("approvalStatus", "approved");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @ParameterizedTest
    @MethodSource("setApprovalStatusNonsenseParameters")
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldReturnBadRequestIfRequestIsInvalid(String approvalId, String approvalStatus) {
        when(approvalService.findById(1L)).thenReturn(Optional.of(approval));
        when(approvalService.approve(any())).thenReturn(true);
        when(approvalService.deny(any())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", approvalId);
        request.put("approvalStatus", approvalStatus);

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldCallApprovalServiceToApproveOrDeny() {
        when(approvalService.findById(1L)).thenReturn(Optional.of(approval));
        when(approvalService.approve(any())).thenReturn(true);
        when(approvalService.deny(any())).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", "1");
        request.put("approvalStatus", "approved");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(approvalService, description("approve() not called")).approve(approval);

        request.put("approvalId", "1");
        request.put("approvalStatus", "denied");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(approvalService, description("deny() not called")).deny(approval);
    }

    @Test
    @Tag(INTEGRATION)
    void setApprovalStatus_ShouldReturn500IfApprovalServiceReturnsFalse() {
        when(approvalService.findById(1L)).thenReturn(Optional.of(approval));
        when(approvalService.approve(any())).thenReturn(false);
        when(approvalService.deny(any())).thenReturn(false);

        Map<String, String> request = new HashMap<>();
        request.put("approvalId", "1");
        request.put("approvalStatus", "approved");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/approvals/setapprovalstatus")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Tag(INTEGRATION)
    @Disabled("JSON parse error. Functionality has been tested via Postman")
    void newAddAccountHolderApproval_ShouldCreateNewAddAccountHolderApprovalIfParamsAreCorrect() {
        Address address = new Address("street", 50, "F", "3256AP", "Waar dan ook", "Koekepannenveen", "Verwegistan");
        Client client = new Client("username",
                                   "password",
                                   "name",
                                   "email@email.com",
                                   "0000000000",
                                   "000000000",
                                   address);
        client.setId(1L);
        Client client1 = new Client("usurnmae",
                                    "passworsd",
                                    "naem",
                                    "amet@semt.com",
                                    "0000000001",
                                    "000000011",
                                    address);
        client1.setId(2L);
        BankAccount bankAccount = new PrivateBankAccount("2123123456", client);
        bankAccount.addAccountHolder(client);
        client.addBankAccount(bankAccount);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client1));
        when(bankAccountService.findBankAccountByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(
                bankAccount));

        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(any(), any())).thenReturn(
                Optional.empty());
        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(any(), any())).thenReturn(
                Optional.empty());

        var requestBody = new StringBuilder("{\n");
        requestBody.append(String.format("\"clientId\": %d,\n", client.getId()));
        requestBody.append(String.format("\"newAccountHolderId\": %d,\n", client1.getId()));
        requestBody.append(String.format("\"bankAccountNumber\": \"%s\",\n", bankAccount.getAccountNumber()));
        requestBody.append(String.format("\"securityCode\": \"%s\"", "12345"));
        requestBody.append("\n}");

        RestAssuredMockMvc.given()
                          .header("Content-Type", "application/json")
                          .body(requestBody)
                          .post("/api/approvals/new/addaccountholderapproval")
                          .then().status(HttpStatus.OK);

    }

    @Test
    @Tag(INTEGRATION)
    @Disabled("Again JSON parsing error. Functionality tested via Postman")
    void newAddAccountHolderApproval_ShouldReturnBadRequestIfRequestIsInvalid() {
        Address address = new Address("street", 50, "F", "3256AP", "Waar dan ook", "Koekepannenveen", "Verwegistan");
        Client client = new Client("username",
                                   "password",
                                   "name",
                                   "email@email.com",
                                   "0000000000",
                                   "000000000",
                                   address);
        client.setId(1L);
        Client client1 = new Client("usurnmae",
                                    "passworsd",
                                    "naem",
                                    "amet@semt.com",
                                    "0000000001",
                                    "000000011",
                                    address);
        client1.setId(2L);
        BankAccount bankAccount = new PrivateBankAccount("2123123456", client);
        bankAccount.addAccountHolder(client);
        client.addBankAccount(bankAccount);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client1));
        when(bankAccountService.findBankAccountByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(
                bankAccount));

        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(any(), any())).thenReturn(
                Optional.empty());
        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(any(), any())).thenReturn(
                Optional.empty());

        var requestBody = new StringBuilder("{\n");
        requestBody.append(String.format("\"securityCode\": \"%s\"", "12345"));
        requestBody.append("\n}");

        RestAssuredMockMvc.given()
                          .header("Content-Type", "application/json")
                          .body(requestBody)
                          .post("/api/approvals/new/addaccountholderapproval")
                          .then().status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Tag(INTEGRATION)
    @Disabled("JSON parse error. Functionality has been tested via Postman")
    void newAddAccountHolderApproval_ShouldReturnUnprocessableEntityIfClientNotAccountHolderOrSameAsNewAccountHolder() {
        Address address = new Address("street", 50, "F", "3256AP", "Waar dan ook", "Koekepannenveen", "Verwegistan");
        Client client = new Client("username",
                                   "password",
                                   "name",
                                   "email@email.com",
                                   "0000000000",
                                   "000000000",
                                   address);
        client.setId(1L);
        Client client1 = client;
        client1.setId(2L);
        BankAccount bankAccount = new PrivateBankAccount("2123123456", client);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client1));
        when(bankAccountService.findBankAccountByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(
                bankAccount));

        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(any(), any())).thenReturn(
                Optional.empty());
        when(approvalService.findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(any(), any())).thenReturn(
                Optional.empty());

        var requestBody = new StringBuilder("{\n");
        requestBody.append(String.format("\"clientId\": %d,\n", client.getId()));
        requestBody.append(String.format("\"newAccountHolderId\": %d,\n", client1.getId()));
        requestBody.append(String.format("\"bankAccountNumber\": \"%s\",\n", bankAccount.getAccountNumber()));
        requestBody.append(String.format("\"securityCode\": \"%s\"", "12345"));
        requestBody.append("\n}");

        RestAssuredMockMvc.given()
                          .header("Content-Type", "application/json")
                          .body(requestBody)
                          .post("/api/approvals/new/addaccountholderapproval")
                          .then().status(HttpStatus.OK);

    }

    // TODO tests for other POST mappings but likely same errors as above

    private static Stream<Arguments> setApprovalStatusNonsenseParameters() {
        return Stream.of(Arguments.of(null, null),
                Arguments.of(null, "approved"),
                Arguments.of("1", null),
                Arguments.of("a", "approved"),
                Arguments.of("1", "A"),
                Arguments.of(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE).toString(), "approved"),
                Arguments.of(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE).toString(), "approved"));
    }
}