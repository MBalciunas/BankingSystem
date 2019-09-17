package com.javatask.banksystem.services;

import com.javatask.banksystem.models.client.Client;
import com.javatask.banksystem.models.client.ClientRepository;
import com.javatask.banksystem.models.transaction.Transaction;
import com.javatask.banksystem.models.transaction.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.javatask.banksystem.utils.Constants.emailValidationRegex;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void signUpAccount(Client client) {
        validateEmail(client);
        clientRepository.save(client);
    }

    private void validateEmail(Client client) {
        String email = client.getEmail();
        if (isEmailTaken(email)) {
            throw new IllegalStateException(String.format("%s email is already taken", email));
        }
        if (!isEmailValid(email)) {
            throw new IllegalStateException(String.format("Email is invalid", email));
        }
    }

    private boolean isEmailTaken(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return client.isPresent();
    }

    private boolean isEmailValid(String email) {
        String emailRegex = emailValidationRegex;
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public long depositAmount(String email, String password, long amount) {
        Client client = login(email, password);
        client.setBalance(client.getBalance() + amount);

        return client.getBalance();
    }

    private Client login(String email, String password) {
        Client client = getClient(email);
        verifyPassword(client, password);
        return client;
    }

    private void verifyPassword(Client client, String password) {
        if(!client.getPassword().equals(password)) {
            throw new IllegalStateException("Incorrect password");
        }
    }

    private Client getClient(String email) {
        Optional<Client> clientOptional = clientRepository.findByEmail(email);
        if (!clientOptional.isPresent()) {
            throw new IllegalStateException("User not found");
        }
        return clientOptional.get();
    }

}
