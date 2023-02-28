package com.dellama.bank.webapi.repository;
import com.dellama.bank.webapi.model.PrivateBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateBankAccountRepository extends JpaRepository<PrivateBankAccount, Long> {

}

