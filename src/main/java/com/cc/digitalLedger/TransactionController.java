package com.cc.digitalLedger;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.NoSuchPaddingException;

@RestController
class TransactionController {

    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/ledger")
    List<Transaction> all() {
        return repository.findAll();
    }

    @GetMapping("/transactions/{publicKey}")
    UserTransactions allWithPublicKey(@PathVariable String publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        List<Transaction> sent = repository.findBySender(publicKey);
        List<Transaction> received = repository.findByReceiver(publicKey);
        return new UserTransactions(publicKey,sent,received);
    }
    @PostMapping("/send")
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        //Check if transaction is valid

        //add transaction to repository
        //return receipt of transaction history
        return repository.save(newTransaction);
//        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @GetMapping("/balance/{publicKey}")
    double getBalanceFromPublicKey(@PathVariable String publicKey) {
        //get all sent and received transactions for given public key.
        //query functions are in TransactionController
        List<Transaction> sent = repository.findBySender(publicKey);
        List<Transaction> received = repository.findByReceiver(publicKey);

        //add received values to balance and subtract sent values
        double balance = 0;
        for(Transaction t : received) {
            balance = balance + t.getAmount();
        }
        for(Transaction t : sent) {
            balance = balance - t.getAmount();
        }
        return balance;
    }
}
