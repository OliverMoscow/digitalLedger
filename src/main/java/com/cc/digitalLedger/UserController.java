package com.cc.digitalLedger;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return repository.findByPublicKey(publicKey)
                .orElseThrow(() -> new InvalidUserExeption("User not found"));

    }

    @GetMapping("/users/name/{name}")
    User fromName(@PathVariable String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new InvalidUserExeption("User not found"));
    }
    @PostMapping("/newUser")
    User newUser(@RequestBody User newUser) {
        //Check if user already exists
        //This function returns an optional user.
        // Will be null if there is a user already found with same email or public key.
        User u = newUser.shouldAddTo(repository)
                .orElseThrow(() -> new InvalidUserExeption());
        User response = repository.save(u);
        //Save backup
        Backup<User> b = new Backup<>(Backup.FileName.users);
        b.backup(repository.findAll());
        return response;
    }
}
