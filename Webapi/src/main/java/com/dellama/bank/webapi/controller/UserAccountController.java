package com.dellama.bank.webapi.controller;

import com.dellama.bank.webapi.DTO.LoginDTO;
import com.dellama.bank.webapi.DTO.LoginResponseDTO;
import com.dellama.bank.webapi.DTO.NewClientDTO;
import com.dellama.bank.webapi.model.*;
import com.dellama.bank.webapi.model.service.UserAccountService;
import com.dellama.bank.webapi.repository.AddressRepository;
import com.dellama.bank.webapi.repository.ClientRepository;
import com.dellama.bank.webapi.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The UserAccountController class provides a REST controller that handles
 * HTTP requests for creating new clients and login into the Llama Bank banking system
 */
@RestController
@RequestMapping("/api/useraccounts")
@Validated
public class UserAccountController {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountRepository userAccountRepository, UserAccountService userAccountService) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public List<UserAccount> getUserAccounts() {
        return userAccountRepository.findAll();
    }

    /**
     * Registers a new client in the system.
     *
     * @param newClient
     * @return the id of the created client
     */
    @PostMapping(value = "/addNewClient")
    public ResponseEntity<String> addNewClient(@Valid @RequestBody NewClientDTO newClient) {
        Address address;
        Client client;

        //check if address is already in DB, if so, it takes that address, otherwise it creates a new address and adds it to DB
        Optional<Address> foundAddress = userAccountService.findAddress(newClient);

        //sets the address
        if (foundAddress.isPresent()) {
            address = foundAddress.get();
        } else {
            address = userAccountService.createNewAddress(newClient);
        }

        //Checks if username already exists
        Optional<UserAccount> userAccount = userAccountService.findUserAccount(newClient.getUserName());

        if (userAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this username already exists.");
        }

        //check if client with BSN already exists, check if address is already in system
        Optional<Client> foundClient = userAccountService.findClientByBSN(newClient.getBsn()); //search user in DB. check if exists (warning: findByBSN returns an optional!

        if (foundClient.isPresent()) { //if exists: return notification that creating user is not possible
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Client with this BSN already exists.");
        } else {     //if not exists: create new client, save client return notification that creation of client is done.
            client = userAccountService.createNewClient(newClient, address);
            return ResponseEntity.ok().body(String.valueOf(client.getId()));
        }
    }

    /**
     * Method to handle Validation Exceptions
     *
     * @param ex
     * @return the validation that failed
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Lets the user login to their account. Checks if the username and password are correct.
     *
     * @param loginDTO
     * @return the id of the user if valid
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        boolean isAccountManager;
        UserAccount user;

        Optional<UserAccount> foundUserAccount = userAccountService.findUserAccount(username);
        if (foundUserAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        } else {
            user = foundUserAccount.get();
        }

        if (user instanceof AccountManager) {
            isAccountManager = true;
        } else {
            isAccountManager = false;
        }

        //check if password is correct
        String password = user.getPassword();
        if (password.equals(loginDTO.getPassword())) {
            loginResponseDTO.setId(String.valueOf(user.getId()));
            loginResponseDTO.setAccountManager(isAccountManager);
            return ResponseEntity.ok().body(loginResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("password not correct");
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public UserAccount findUserAccountById(@PathVariable Long id) {
        UserAccount userAccount = userAccountService.findUserAccountById(id);
        return userAccount;
    }
}


