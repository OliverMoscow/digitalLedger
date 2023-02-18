package com.cc.digitalLedger;

public class InvalidTransactionExeption extends RuntimeException {
    InvalidTransactionExeption(String message) {
        super("Error: Invalid Transaction. " + message);
    }
}
