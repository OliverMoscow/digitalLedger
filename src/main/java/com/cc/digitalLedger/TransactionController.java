package com.cc.digitalLedger;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TransactionController {

    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/send")
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        //Check if transaction is valid
        //transaction should have a previous transaction
        //add transaction to repository
        //return receipt of transaction history
        return repository.save(newTransaction);
//        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
