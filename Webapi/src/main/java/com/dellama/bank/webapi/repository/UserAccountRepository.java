package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserName(String username);
    UserAccount findUserAccountById(Long id);

}
