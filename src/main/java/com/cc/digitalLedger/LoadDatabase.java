package com.cc.digitalLedger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    @Bean
    CommandLineRunner initLedgerDatabase(TransactionRepository repository) {
        return args -> {
            Backup<Transaction> backup = new Backup<>(Backup.FileName.ledger);
            List<Transaction> data = backup.load();
            if(data != null) {
                System.out.println("Recovering... Initialized database with transactions: ");
                for(Transaction t: data) {
                    repository.save(t);
                    System.out.println(t);
                }
            }
        };
    }
    @Bean
    CommandLineRunner initUsersDatabase(UserRepository repository) {
        return args -> {
            Backup<User> backup = new Backup<>(Backup.FileName.users);
            List<User> data = backup.load();
            if(data != null) {
                System.out.println("Recovering... Initialized database with users: ");
                for(User u: data) {
                    repository.save(u);
                    System.out.println(u.getName());
                }
            }
        };
    }
}
