package com.javatask.banksystem.controllers;

import com.javatask.banksystem.models.client.Client;
import com.javatask.banksystem.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("clients")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody Client client) {
        try {
            clientService.signUpAccount(client);
        } catch (IllegalStateException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestBody Map<String, String> request) {
        long balance;
        try {
            balance = clientService.depositAmount(request.get("email"), request.get("password"), Long.parseLong(request.get("amount")));
        } catch (IllegalStateException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(balance, HttpStatus.OK);
    }
}
