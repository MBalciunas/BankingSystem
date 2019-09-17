package com.javatask.banksystem.services.unit;

import com.javatask.banksystem.models.client.Client;
import com.javatask.banksystem.models.client.ClientRepository;
import com.javatask.banksystem.services.ClientService;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
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

    @Test
    public void depositAmountTest() {
        Client client = Client.builder().email("e").password("e").build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(client.getEmail())).willReturn(Optional.of(client));
        ClientService clientService = new ClientService(clientRepository);

        long balance = clientService.depositAmount(client.getEmail(), client.getPassword(), 100);

        assertEquals(100, balance);
    }

    @Test(expected = IllegalStateException.class)
    public void depositAmountTest_EmailNotFound() {
        Client client = Client.builder().build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findById(1L)).willReturn(Optional.empty());

        ClientService clientService = new ClientService(clientRepository);

        long balance = clientService.depositAmount(client.getEmail(), client.getPassword(), 100);

        fail("Client with email wasn't found");
    }

    @Test
    public void withdrawAmountTest() {
        Client client = Client.builder().email("e").password("e").balance(1000).build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(client.getEmail())).willReturn(Optional.of(client));

        ClientService clientService = new ClientService(clientRepository);
        long balance = clientService.withdrawAmount(client.getEmail(), client.getPassword(), 100);

        assertEquals(900, balance);
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawAmountTest_EmailNotFound() {
        Client client = Client.builder().build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findById(1L)).willReturn(Optional.empty());

        ClientService clientService = new ClientService(clientRepository);
        long balance = clientService.withdrawAmount(client.getEmail(), client.getPassword(), 100);

        fail("Client with email wasn't found");
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawAmountTest_NotEnoughFunds() {
        Client client = Client.builder().email("e").password("e").build();
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findByEmail(client.getEmail())).willReturn(Optional.of(client));

        ClientService clientService = new ClientService(clientRepository);
        long balance = clientService.withdrawAmount(client.getEmail(), client.getPassword(), 100);

        fail("Not enough funds");
    }
}
