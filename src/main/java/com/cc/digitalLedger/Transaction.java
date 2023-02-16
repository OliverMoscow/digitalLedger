package com.cc.digitalLedger;


import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "TRANSACTION_DETAILS")
class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SENDER")
    private String sender;
    @Column(name = "RECEIVER")
    private String receiver;
    @Column(name = "AMOUNT")
    private Double amount;

    public Transaction() {}

    public Transaction(String sender, String receiver, Double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Double getAmount() {
        return amount;
    }


}
