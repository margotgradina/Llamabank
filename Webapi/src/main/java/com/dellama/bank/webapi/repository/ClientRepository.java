package com.dellama.bank.webapi.repository;
import com.dellama.bank.webapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByBsn(String bsn);

    Optional<Client> findById(Long id);



}
