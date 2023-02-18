package com.cc.digitalLedger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySender(String SENDER);
    List<Transaction> findByReceiver(String RECEIVER);


}