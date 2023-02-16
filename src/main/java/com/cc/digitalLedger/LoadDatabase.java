package com.cc.digitalLedger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TransactionRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Transaction("000", "111",82.85)));
            log.info("Preloading " + repository.save(new Transaction("000", "222",17.35)));
            log.info("Preloading " + repository.save(new Transaction("111", "222",3.02)));
            log.info("Preloading " + repository.save(new Transaction("222", "000",32.54)));
        };
    }
}
