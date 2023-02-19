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
import java.util.Optional;

public class Decrypter {
    private NewTransaction input;
    public Decrypter(NewTransaction input) {
        this.input = input;
    }

    public DecryptResponse decrypt() {
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
            byte[] decryptedBytes = cipher.doFinal(encryptedMessage.getBytes());
            String decryptedMessage = new String(decryptedBytes);
            Transaction t = new Transaction(publicKey, "receiver", 0.00);
            response.transaction = Optional.of(t);
            if(notEnoughFunds(t)) {
                response.transaction = null;
                response.errorMessage += "User does not have enough funds. ";
            }
            if(senderNotFound(t)) {
                response.transaction = null;
                response.errorMessage += "Could not find sender in db. Please add a new user first. ";
            }
            if(receiverNotFound(t)) {
                response.transaction = null;
                response.errorMessage += "Could not find receiver in db. Make sure you are sending to a user with an existing account.  ";
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

    private boolean receiverNotFound(Transaction t) {
        return false;
    }

    private boolean senderNotFound(Transaction t) {
        return false;
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
