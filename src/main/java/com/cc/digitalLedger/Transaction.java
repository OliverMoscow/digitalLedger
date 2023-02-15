package com.cc.digitalLedger;


import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_details")
class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String sender;
    private String reciever;
    private Double amount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "previous_transaction")
    private Transaction previous;

    Transaction() {}

    public Transaction(String sender, String reciever, Double amount, Transaction previous) {
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.previous = previous;
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReciever() {
        return reciever;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction getPrevious() {
        return previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && sender.equals(that.sender) && reciever.equals(that.reciever) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, reciever, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", amount=" + amount +
                '}';
    }
}
