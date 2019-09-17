package com.javatask.banksystem.services;

import com.javatask.banksystem.models.transaction.Transaction;
import com.javatask.banksystem.models.transaction.TransactionRepository;
import com.javatask.banksystem.models.transaction.TransactionType;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveNewTransaction(long amount, TransactionType transactionType) {
        Transaction transaction =  Transaction.builder().amount(amount).transactionType(transactionType).build();
        return transactionRepository.save(transaction);
    }
}
