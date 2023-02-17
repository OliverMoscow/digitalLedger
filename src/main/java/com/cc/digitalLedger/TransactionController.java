package com.cc.digitalLedger;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

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
    Transaction send(@RequestBody NewTransaction data) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        //Check if transaction is valid
        //convert newTransaction to Transaction
        decrypt(data);


        //save backup of ledger
        //add transaction to repository
        //repository.save(data)
        //return added transaction
        return null;
        //.orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    NewTransaction decrypt(NewTransaction data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
            NewTransaction newT = data;
            //convert public key from string type to Public Key type
            String publicKey = newT.sender;
            byte[] publicBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);

            String encryptedMessage = newT.encrypted;
            byte[] decryptedBytes = cipher.doFinal(encryptedMessage.getBytes());
            String decryptedMessage = new String(decryptedBytes);

            String receiver = newT.receiver;
            double amount = newT.amount;

            data = new Transaction(publicKey, decryptedMessage, receiver, amount);

        return data;
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
    String receiver;
    double amount;

    public NewTransaction(String sender, String encrypted, String receiver, double amount) {
        this.sender = sender;
        this.encrypted = encrypted;
        this.receiver = receiver;
        this.amount = amount;
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

