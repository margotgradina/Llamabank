package com.dellama.bank.webapi.repository;


import com.dellama.bank.webapi.model.AccountManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountManagerRepository extends JpaRepository<AccountManager, Long> {

    @Query(value = "SELECT am.isSmallBusinessManager FROM AccountManager am\n" +
            "WHERE am.id =:id")
    Boolean isSmallBusinessManager(@Param("id") Long id);


}

