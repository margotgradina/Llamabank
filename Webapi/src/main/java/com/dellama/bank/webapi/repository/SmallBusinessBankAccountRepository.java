package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.DTO.BankAccountDTO;
import com.dellama.bank.webapi.model.BankAccount;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmallBusinessBankAccountRepository extends JpaRepository<SmallBusinessBankAccount, Long> {


}
