package com.cc.digitalLedger;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    UserTransactions allWithPublicKey(@PathVariable String publicKey) {
        List<Transaction> sent = repository.findBySender(publicKey);
        List<Transaction> received = repository.findByReceiver(publicKey);
        return new UserTransactions(publicKey,sent,received);
    }
    @PostMapping("/send")
    Transaction send(@RequestBody NewTransaction data) {
        //Check if transaction is valid
        //convert newTransaction to Transaction
        Decrypter decrypter = new Decrypter(data);
        DecryptResponse response = decrypter.decrypt();
        Transaction t = response.transaction
                .orElseThrow(() -> new InvalidTransactionExeption(response.errorMessage));

        //add transaction to repository
        repository.save(t);
        //save backup of ledger
        Backup<Transaction> b = new Backup<>(Backup.FileName.ledger);
        b.backup(repository.findAll());
        //return added transaction
        return t;
    }


    @GetMapping("/balance/{publicKey}")
    double getBalanceFromPublicKey(@PathVariable String publicKey) {
        //get all sent and received transactions for given public key.
        //query functions are in TransactionController
        return balance(publicKey);
    }

    private double balance(String publicKey) {
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

class NewTransaction {
    String sender;
    String encrypted;

    public NewTransaction(String sender, String encrypted) {
        this.sender = sender;
        this.encrypted = encrypted;
    }
}


class UserTransactions {
    public String sender;
    public List<Transaction> sent;
    public List<Transaction> received;

    public UserTransactions(String publicKey, List<Transaction> sent, List<Transaction> received) {
        this.sender = publicKey;
        this.sent = sent;
        this.received = received;
    }
}


