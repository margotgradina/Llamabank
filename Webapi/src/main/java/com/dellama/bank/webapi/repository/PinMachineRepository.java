package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.model.PinMachine;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PinMachineRepository extends JpaRepository<PinMachine, Long> {

    Optional<PinMachine> findPinMachineByConnectionCodeAndSmallBusinessBankAccount(int connectionCode, SmallBusinessBankAccount smallBusinessBankAccount);
}
