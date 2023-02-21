package com.cc.digitalLedger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;


public class Decrypter {
    private NewTransaction input;
    public Decrypter(NewTransaction input) {
        this.input = input;
    }

    public DecryptResponse decrypt(UserRepository repository) {
        DecryptResponse response = new DecryptResponse("Could not decrypt transaction. ", null);
        try {
            //convert public key from string type to Public Key type
            String publicKey = input.sender;
            byte[] publicBytes = Base64.getDecoder().decode(publicKey) ;
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);

            String encryptedMessage = input.encrypted;
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage); // decode from base64 format
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedMessage = new String(decryptedBytes);
            //extract the username and amount
            JSONObject json = new JSONObject(decryptedMessage);
            String receiver = json.getString("receiver");
            double amount = json.getDouble("amount");
            //search for user based on receiver username
            List<User> r = repository.findByName(receiver);
            if (r.size() != 1) {
                response.transaction = null;
                response.errorMessage += "Could not find receiver in db. Make sure you are sending to a user with an existing account.";
                return response;
            }
            receiver = r.get(0).getPublicKey();
            //create a new transaction based on that info
            Transaction t = new Transaction(input.sender, receiver, amount);
            response.transaction = Optional.of(t);
            if(notEnoughFunds(t)) {
                response.transaction = null;
                response.errorMessage += "User does not have enough funds. ";
            }
            //Catch every exception and update error message
        }  catch (NoSuchPaddingException e) {
            response.errorMessage += e.getMessage();
        } catch (IllegalBlockSizeException e) {
            response.errorMessage += e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            response.errorMessage += e.getMessage();
        } catch (InvalidKeySpecException e) {
            response.errorMessage += e.getMessage();
        } catch (BadPaddingException e) {
            response.errorMessage += e.getMessage();
        } catch (InvalidKeyException e) {
            response.errorMessage += e.getMessage();
        }
        return response;
    }

    private boolean notEnoughFunds(Transaction t) {
        return false;
    }
}

class DecryptResponse {
    public String errorMessage;
    public Optional<Transaction> transaction;

    public DecryptResponse(String errorMessage, Optional<Transaction> transaction) {
        this.errorMessage = errorMessage;
        this.transaction = transaction;
    }
}
