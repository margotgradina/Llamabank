package com.dellama.bank.webapi;

import com.dellama.bank.webapi.dummydata.AbstractFactory;
import com.dellama.bank.webapi.dummydata.FactoryProvider;
import com.dellama.bank.webapi.dummydata.FactoryType;
import com.dellama.bank.webapi.model.*;
import com.dellama.bank.webapi.model.approval.AddAccountHolderApproval;
import com.dellama.bank.webapi.model.approval.OpenBankAccountApproval;
import com.dellama.bank.webapi.model.approval.OpenBankAccountType;
import com.dellama.bank.webapi.model.approval.PinMachineApproval;
import com.dellama.bank.webapi.model.iban.IbanUtils;
import com.dellama.bank.webapi.model.service.ApprovalService;
import com.dellama.bank.webapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Configuration
public class DummyData {
    private static final Logger LOG = LoggerFactory.getLogger(DummyData.class);
    private static final int PRIVATE_CLIENT_TOTAL = 40;
    private static final int PRIVATE_CLIENT_WITH_2_BANK_ACCOUNTS = PRIVATE_CLIENT_TOTAL / 2;
    private static final int PRIVATE_CLIENT_WITH_3_BANK_ACCOUNTS = PRIVATE_CLIENT_WITH_2_BANK_ACCOUNTS / 2;
    private static final int PRIVATE_CLIENT_THAT_WANTS_ANOTHER = PRIVATE_CLIENT_WITH_2_BANK_ACCOUNTS / 2;
    private static final int SMALL_BUSINESS_CLIENTS_TOTAL = 10;
    private static final int TRANSACTIONS_PER_ACCOUNT = 20;
    private static final double MAXIMUM_TRANSACTION_AMOUNT = 2000.00;
    public static final int PIN_MACHINE_TOTAL = SMALL_BUSINESS_CLIENTS_TOTAL / 2;

    @Value("${dummydata.populate}")
    private boolean populate;

    @Bean
    CommandLineRunner initDatabase(UserAccountRepository userAccountRepository,
                                   ApprovalService approvalService,
                                   BankAccountRepository bankAccountRepository,
                                   AddressRepository addressRepository,
                                   TransactionRepository transactionRepository,
                                   ClientRepository clientRepository,
                                   SmallBusinessBankAccountRepository smallBusinessBankAccountRepository) {
        return args -> {
            if (!populate) {
                return; // Only populate with dummy data if populate is true
            }

            Random random = new Random();

            AbstractFactory<Client> clientAbstractFactory = FactoryProvider.getFactory(FactoryType.CLIENT);

            // PRIVATE CLIENTS
            LOG.info("--- PRIVATE CLIENTS AND BANK ACCOUNTS ---");
            LOG.info(String.format("    - %d first bank accounts", PRIVATE_CLIENT_TOTAL));
            List<Client> privateClients = clientAbstractFactory.create(PRIVATE_CLIENT_TOTAL);

            for (Client privateClient : privateClients) {
                addressRepository.save(privateClient.getAddress());
                privateClient = userAccountRepository.save(privateClient);

                var openBankAccountApproval = new OpenBankAccountApproval(privateClient,
                                                                          OpenBankAccountType.PRIVATE,
                                                                          null);
                approvalService.approve(openBankAccountApproval);
                LOG.info(privateClient.getId().toString());
            }

            // half have 2 accounts
            LOG.info(String.format("    - %d second accounts", PRIVATE_CLIENT_WITH_2_BANK_ACCOUNTS));
            List<Client> firstHalfPrivateClients = privateClients.stream()
                                                          .limit(PRIVATE_CLIENT_WITH_2_BANK_ACCOUNTS)
                                                          .collect(Collectors.toList());
            for (Client clientOfTheFirstHalf : firstHalfPrivateClients) {
                addressRepository.save(clientOfTheFirstHalf.getAddress());
                userAccountRepository.save(clientOfTheFirstHalf);

                var openBankAccountApproval = new OpenBankAccountApproval(clientOfTheFirstHalf,
                                                                          OpenBankAccountType.PRIVATE,
                                                                          null);
                approvalService.approve(openBankAccountApproval);
                LOG.info(clientOfTheFirstHalf.getId().toString());
            }

            // a quarter has 3 accounts
            LOG.info(String.format("    - %d third accounts", PRIVATE_CLIENT_WITH_3_BANK_ACCOUNTS));
            List<Client> firstQuarterPrivateClients = privateClients.stream()
                                                          .limit(PRIVATE_CLIENT_WITH_3_BANK_ACCOUNTS)
                                                          .collect(Collectors.toList());
            for (Client clientOfTheFirstQuarter : firstQuarterPrivateClients) {
                addressRepository.save(clientOfTheFirstQuarter.getAddress());
                userAccountRepository.save(clientOfTheFirstQuarter);

                var openBankAccountApproval = new OpenBankAccountApproval(clientOfTheFirstQuarter,
                                                                          OpenBankAccountType.PRIVATE,
                                                                          null);
                approvalService.approve(openBankAccountApproval);
                LOG.info(clientOfTheFirstQuarter.getId().toString());
            }

            // and an eigtht wants another bank account
            LOG.info(String.format("    - %d want another", PRIVATE_CLIENT_THAT_WANTS_ANOTHER));
            List<Client> fourthWanters = privateClients.stream()
                                                       .limit(PRIVATE_CLIENT_WITH_3_BANK_ACCOUNTS)
                                                       .collect(Collectors.toList());
            for (Client fourthWanter : fourthWanters) {
                addressRepository.save(fourthWanter.getAddress());
                userAccountRepository.save(fourthWanter);

                var openBankAccountApproval = new OpenBankAccountApproval(fourthWanter,
                                                                          OpenBankAccountType.PRIVATE,
                                                                          null);
                approvalService.save(openBankAccountApproval);
                LOG.info(fourthWanter.getId().toString());
            }
            LOG.info("    # DONE");


            // EXTRA ACCOUNT HOLDERS
            LOG.info("--- EXTRA ACCOUNT HOLDERS ---");
            if (privateClients.size() > 4) {
                int offset = privateClients.size() / 2;
                for (int i = 0; i < offset; i++) {
                    Client owner = privateClients.get(i);
                    Client newAccountHolder = privateClients.get(i + offset);

                    BankAccount bankAccount = owner.getBankAccounts().stream().findAny().get();

                    var addAccountHolderApproval = new AddAccountHolderApproval(owner,
                                                                                newAccountHolder,
                                                                                bankAccount,
                                                                                "12345");
                    approvalService.save(addAccountHolderApproval);

                    if (i % 2 == 0) {
                        approvalService.approve(addAccountHolderApproval);

                        if (i % 4 == 0) {
                            newAccountHolder.addBankAccount(bankAccount);
                            bankAccount.addAccountHolder(newAccountHolder);
                            clientRepository.save(newAccountHolder);
                            bankAccountRepository.save(bankAccount);
                            addAccountHolderApproval.setConsumed(true);
                            approvalService.save(addAccountHolderApproval);
                        }
                    }
                }
            }


            // SMALL BUSINESS CLIENTS
            LOG.info("--- SMALL BUSINESS CLIENTS AND BANK ACCOUNTS ---");
            List<Client> smallBusinessClients = clientAbstractFactory.create(SMALL_BUSINESS_CLIENTS_TOTAL);

            for (Client smallBusinessClient : smallBusinessClients) {
                addressRepository.save(smallBusinessClient.getAddress());
                smallBusinessClient = userAccountRepository.save(smallBusinessClient);

                Sector sector = Sector.values()[random.nextInt(0, Sector.values().length)];

                var openBankAccountApproval = new OpenBankAccountApproval(smallBusinessClient,
                                                                          OpenBankAccountType.BUSINESS,
                                                                          sector);

                approvalService.approve(openBankAccountApproval);
                LOG.info(smallBusinessClient.getId().toString());
            }
            LOG.info("    # DONE");

            // ACCOUNT MANAGERS
            LOG.info("--- ACCOUNT MANAGERS ---");
            var privateAccountManager = new AccountManager("privateAccountManager", "password", "Axel Manger", false);
            var smallBusinessAccountManager = new AccountManager("smallBusinessAccountManager",
                                                                 "password",
                                                                 "Piet Manger",
                                                                 true);

            userAccountRepository.save(privateAccountManager);
            LOG.info(privateAccountManager.getId().toString());
            userAccountRepository.save(smallBusinessAccountManager);
            LOG.info(smallBusinessAccountManager.getId().toString());
            LOG.info("    # DONE");

            // TRANSACTIONS
            LOG.info("--- TRANSACTIONS ---");
            List<BankAccount> bankAccounts = bankAccountRepository.findAll();

            Map<BankAccount, List<Transaction>> accountTransactionMap = new HashMap<>();

            for (BankAccount bankAccount : bankAccounts) {
                LOG.info(String.format("    - Transactions for %s", bankAccount.getAccountNumber()));

                for (int i = 0; i < TRANSACTIONS_PER_ACCOUNT; i++) {
                    var targetBankAccount = bankAccounts.stream()
                                                        .filter(b -> !b.getAccountNumber()
                                                                       .equals(bankAccount.getAccountNumber()))
                                                        .toList()
                                                        .get(random.nextInt(bankAccounts.size() - 1));

                    var amount = Math.round(random.nextDouble(MAXIMUM_TRANSACTION_AMOUNT) * 100.0) / 100.0;

                    String thisIban = IbanUtils.internalBankAccountNumberToIban(bankAccount.getAccountNumber());
                    String targetIban = IbanUtils.internalBankAccountNumberToIban(targetBankAccount.getAccountNumber());

                    Transaction transaction;
                    String description = String.format("Transaction %d", i);
                    LocalDateTime date = LocalDateTime.now();

                    // every 3rd or so transaction is a withdrawal
                    if (i % 3 == 0) {
                        transaction = new Transaction(thisIban, targetIban, amount, description, date);
                    } else {
                        transaction = new Transaction(targetIban, thisIban, amount, description, date);
                    }

                    accountTransactionMap.putIfAbsent(bankAccount, new ArrayList<>());
                    accountTransactionMap.putIfAbsent(targetBankAccount, new ArrayList<>());

                    accountTransactionMap.get(bankAccount).add(transaction);
                    accountTransactionMap.get(targetBankAccount).add(transaction);
                    LOG.info(String.valueOf(i));
                }

                for (Map.Entry<BankAccount, List<Transaction>> entry : accountTransactionMap.entrySet()) {
                    List<Transaction> transactions = entry.getValue();
                    entry.getKey().setBalance(calculateBalance(transactions));
                    transactionRepository.saveAll(entry.getValue());
                    bankAccountRepository.save(entry.getKey());
                }

            }
            LOG.info("    # DONE");

            // PIN MACHINES
            LOG.info("--- PIN MACHINES ---");
            List<SmallBusinessBankAccount> firstSomeAmountSmallBusinessBankAccounts = smallBusinessClients.stream()
                                                                                                   .limit(PIN_MACHINE_TOTAL)
                                                                                                   .flatMap(s -> s.getBankAccounts()
                                                                                                                  .stream())
                                                                                                   .map(b -> (SmallBusinessBankAccount) b)
                                                                                                   .collect(Collectors.toList());

            for (int i = 0; i < firstSomeAmountSmallBusinessBankAccounts.size(); i++) {
                SmallBusinessBankAccount sbAccount = firstSomeAmountSmallBusinessBankAccounts.get(i);
                PinMachineApproval pinMachineApproval = new PinMachineApproval(sbAccount.getAccountHolders()
                                                                                        .stream()
                                                                                        .findFirst()
                                                                                        .get(), sbAccount);
                if (i % 2 == 0) {
                    approvalService.approve(pinMachineApproval);
                } else {
                    approvalService.save(pinMachineApproval);
                }
                LOG.info(sbAccount.getId().toString());
            }

            LOG.info("    # DONE");
        };
    }

    double calculateBalance(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }
}
