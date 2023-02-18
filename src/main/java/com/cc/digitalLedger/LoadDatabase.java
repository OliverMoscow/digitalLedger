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
                for(Transaction t: data) {
                    log.info("Preloading " + repository.save(t));
                }
            }
        };
    }
    @Bean
    CommandLineRunner initUsersDatabase(UserRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new User("Vincent", "195609")));
            log.info("Preloading " + repository.save(new User("Oliver", "904359")));
            log.info("Preloading " + repository.save(new User("Kalie", "320197")));
        };
    }
}
