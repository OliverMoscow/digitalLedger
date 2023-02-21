package com.cc.digitalLedger;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    Controller(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/ledger")
    List<Transaction> all() {
        return transactionRepository.findAll();
    }

    @GetMapping("/transactions/{publicKey}")
    UserTransactions allWithPublicKey(@PathVariable String publicKey) {
        List<Transaction> sent = transactionRepository.findBySender(publicKey);
        List<Transaction> received = transactionRepository.findByReceiver(publicKey);
        return new UserTransactions(publicKey,sent,received);
    }
    @PostMapping("/send")
    Transaction send(@RequestBody NewTransaction data) {
        //Check if transaction is valid
        //Decrypter will return an optional Transaction from a NewTransaction
        Decrypter decrypter = new Decrypter(data);
        DecryptResponse response = decrypter.decrypt(userRepository);
        Transaction t = response.transaction
                .orElseThrow(() -> new InvalidTransactionExeption(response.errorMessage));

        //add transaction to repository
        transactionRepository.save(t);
        //save backup of ledger
        Backup<Transaction> b = new Backup<>(Backup.FileName.ledger);
        b.backup(transactionRepository.findAll());
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
        List<Transaction> sent = transactionRepository.findBySender(publicKey);
        List<Transaction> received = transactionRepository.findByReceiver(publicKey);

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

    //USER ENDPOINTS
    @GetMapping("/users")
    List<User> allUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    User fromKey(@RequestBody User u) {
//        if (u.getPublicKey() != null) {
//            return repository.findByPublicKey(u.getPublicKey())
//                    .orElseThrow(() -> new InvalidUserExeption("User not found"));
//        }
//        if (u.getName() != null) {
//            return repository.findByPublicKey(u.getPublicKey())
//                    .orElseThrow(() -> new InvalidUserExeption("User not found"));
//        }
        throw new InvalidUserExeption("no username or public key provided");
    }

    @GetMapping("/users/name/{name}")
    User fromName(@PathVariable String name) {
        List<User> res = userRepository.findByName(name);
        if(res.size() == 1) {
            return res.get(0);
        }
        return null;
//        return repository.findByName(name)
//                .orElseThrow(() -> new InvalidUserExeption("User not found"));
    }
    @PostMapping("/newUser")
    User newUser(@RequestBody User newUser) {
        //Check if user already exists
        //This function returns an optional user.
        // Will be null if there is a user already found with same email or public key.
        if (newUser.shouldAddTo(userRepository)) {
            User response = userRepository.save(newUser);
            //Save backup
            Backup<User> b = new Backup<>(Backup.FileName.users);
            b.backup(userRepository.findAll());
            return response;
        } else {
            return null;
        }
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


