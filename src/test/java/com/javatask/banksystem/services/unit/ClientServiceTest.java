package com.javatask.banksystem.services.unit;

import com.javatask.banksystem.models.client.Client;
import com.javatask.banksystem.models.client.ClientRepository;
import com.javatask.banksystem.services.ClientService;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ClientServiceTest {

    @Test
    public void signUpClient_successful() {
        String email = "john@gmail.com";
        String password = "password";
        Client client = Client.builder().email(email).password(password).build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(password)).willReturn(Optional.empty());
        ClientService clientService = new ClientService(clientRepository);

        clientService.signUpAccount(client);
    }

    @Test(expected = IllegalStateException.class)
    public void signUpClient_invalidEmail() {
        String email = "johmgmail.com";
        String password = "password";
        Client client = Client.builder().email(email).password(password).build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(email)).willReturn(Optional.empty());
        ClientService clientService = new ClientService(clientRepository);

        clientService.signUpAccount(client);
        fail("Can't sign up with invalid email");
    }

    @Test(expected = IllegalStateException.class)
    public void signUpClient_emailTaken() {
        String email = "john@gmail.com";
        String password = "password";
        Client client = Client.builder().email(email).password(password).build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(email)).willReturn(Optional.of(Client.builder().build()));
        ClientService clientService = new ClientService(clientRepository);

        clientService.signUpAccount(client);
        fail("Can't sign up with taken email");
    }
}
