package com.cc.digitalLedger;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "LEDGER")
class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SENDER", columnDefinition = "Text", length = 2000)
    private String sender;
    @Column(name = "RECEIVER", columnDefinition = "Text", length = 2000)
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
