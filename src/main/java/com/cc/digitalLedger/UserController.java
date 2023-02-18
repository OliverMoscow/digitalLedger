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
class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @GetMapping("/users/{publicKey}")
    User fromKey(@PathVariable String publicKey) {
        return repository.findByPublicKey(publicKey);
    }

    @GetMapping("/users/{name}")
    User fromName(@PathVariable String name) {
        return repository.findByName(name);
    }
    @PostMapping("/newUser")
    User newEmployee(@RequestBody User newUser) {
        //TODO - Check if user already exists
        //TODO - Throw and error if it doesn't
        //Add new user
        User res = repository.save(newUser);
        //Save backup
        Backup<User> b = new Backup<>(Backup.FileName.users);
        b.backup(repository.findAll());
        return res;
    }
}
