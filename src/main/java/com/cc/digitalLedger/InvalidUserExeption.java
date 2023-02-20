package com.cc.digitalLedger;

public class InvalidUserExeption extends RuntimeException {
    InvalidUserExeption() {
        super("Invalid User: User cannot be added because there is already a user with the same public key or name");
    }
    InvalidUserExeption(String s) {
        super(s);
    }
}
