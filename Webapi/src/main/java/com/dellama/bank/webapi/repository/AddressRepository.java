package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByPostalCodeAndHouseNumberAndCountry(String postalCode, int houseNumber, String country);

}
