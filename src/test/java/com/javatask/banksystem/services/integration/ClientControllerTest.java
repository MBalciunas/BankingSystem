package com.javatask.banksystem.services.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void singUpDeposit() throws Exception {

        String email = "john@gmail.com";
        String password = "password";
        String client = getClientJson(email, password);
        String clientDeposit = getClientJson(email, password, 1000);
        String clientWithdraw = getClientJson(email, password, 500);

        mockMvc.perform(post("/clients/signup")
            .contentType("application/json")
        .content(client))
        .andExpect(status().isOk());

        mockMvc.perform(post("/clients/deposit")
                .contentType("application/json")
                .content(clientDeposit))
                .andExpect(status().isOk())
        .andExpect(content().string("1000"));
    }

    @Test
    public void invalidSignUpEmail() throws Exception {
        String email = "johngmail.com";
        String password = "password";
        String client = getClientJson(email, password);

        mockMvc.perform(post("/clients/signup")
                .contentType("application/json")
                .content(client))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signUpEmailTaken() throws Exception {
        String email = "john2@gmail.com";
        String password = "password";
        String client = getClientJson(email, password);

        mockMvc.perform(post("/clients/signup")
                .contentType("application/json")
                .content(client))
                .andExpect(status().isOk());

        mockMvc.perform(post("/clients/signup")
                .contentType("application/json")
                .content(client))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void depositWrongPassword() throws Exception {
        String email = "john3@gmail.com";
        String password = "password";
        String client = getClientJson(email, password);
        String clientBadPasswordDeposit = getClientJson(email, password + "Bad", 1000);

        mockMvc.perform(post("/clients/signup")
                .contentType("application/json")
                .content(client))
                .andExpect(status().isOk());

        mockMvc.perform(post("/clients/deposit")
                .contentType("application/json")
                .content(clientBadPasswordDeposit))
                .andExpect(status().isBadRequest());
    }

    private String getClientJson(String email, String password) {
        return String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);
    }

    private String getClientJson(String email, String password, long amount) {
        return String.format("{\"email\":\"%s\", \"password\":\"%s\", \"amount\":%d}", email, password, amount);
    }
}
