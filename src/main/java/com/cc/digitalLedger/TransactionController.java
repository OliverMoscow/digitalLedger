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
    Transaction send(@RequestBody NewTransaction data) {
        //Check if transaction is valid
        //convert newTransaction to Transaction
//        try {
//            Transaction decrypted = decrypt(data);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        } catch (SignatureException e) {
//            throw new RuntimeException(e);
//        }
        //save backup of ledger
        //add transaction to repository
//        repository.save(data)
        //return added transaction
        return repository.save(new Transaction("alice", "bob", 12.20));
//        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    Transaction decrypt(Transaction data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
//        Signature sig = Signature.getInstance("SHA256withRSA");
//        //convert sender to rsa key
//        byte[] encoded = Base64.decodeBase64(data.sender);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(keySpec);
//
//        sig.initVerify(publicKey);
//        sig.update(data.encrypted.getBytes());
//        byte[] signature = Base64.decodeBase64(data.encrypted);
//        String message = signature.toString();
//        boolean verified = sig.verify(signature);
//        System.out.println("Signature verified: " + verified);
        return null;
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

