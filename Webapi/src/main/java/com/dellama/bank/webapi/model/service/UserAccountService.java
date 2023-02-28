package com.dellama.bank.webapi.model.service;

import com.dellama.bank.webapi.DTO.NewClientDTO;
import com.dellama.bank.webapi.model.Address;
import com.dellama.bank.webapi.model.Client;
import com.dellama.bank.webapi.model.UserAccount;
import com.dellama.bank.webapi.repository.AddressRepository;
import com.dellama.bank.webapi.repository.ClientRepository;
import com.dellama.bank.webapi.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {
    ClientRepository clientRepository;
    AddressRepository addressRepository;
    UserAccountRepository userAccountRepository;

    public UserAccountService(ClientRepository clientRepository, UserAccountRepository userAccountRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public Optional<Address> findAddress(NewClientDTO newClientDTO) {
        return addressRepository.findByPostalCodeAndHouseNumberAndCountry
                (newClientDTO.getPostalCode(), newClientDTO.getHouseNumber(), newClientDTO.getCountry());
    }

    public Address createNewAddress(NewClientDTO newClientDTO) {
        return new Address(newClientDTO.getStreet(), newClientDTO.getHouseNumber(), newClientDTO.getHouseNumberExtension(),
                newClientDTO.getPostalCode(), newClientDTO.getCity(), newClientDTO.getRegion(), newClientDTO.getCountry());
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<UserAccount> findUserAccount(String username) {
        return Optional.ofNullable(userAccountRepository.findByUserName(username));
    }

    public UserAccount findUserAccountById(Long id) {
        return userAccountRepository.findUserAccountById(id);
    }

    public Optional<Client> findClientByBSN(String bsn) {
        return clientRepository.findByBsn(bsn);
    }

    public Optional<Client> findClientById(Long id){
        return clientRepository.findById(id);
    }

    public Client createNewClient(NewClientDTO newClientDTO, Address address) {
        this.saveAddress(address);
        Client client = new Client(newClientDTO.getUserName(), newClientDTO.getPassword(), newClientDTO.getName(),
                newClientDTO.getEmail(), newClientDTO.getPhoneNumber(), newClientDTO.getBsn(), address);
        return this.saveClient(client);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}
