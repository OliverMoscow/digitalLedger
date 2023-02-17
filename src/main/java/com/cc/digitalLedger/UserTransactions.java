package com.cc.digitalLedger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.List;

//Class to format response in /transactions/{publicKey}
public class UserTransactions {
    public String sender;
    public List<Transaction> sent;
    public List<Transaction> received;




    public UserTransactions(String publicKey, List<Transaction> sent, List<Transaction> received)  {

        this.sender = publicKey;
        this.sent = sent;
        this.received = received;


    }
}
