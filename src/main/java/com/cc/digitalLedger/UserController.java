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

    @PostMapping("/users")
    User fromKey(@RequestBody User u) {
//        if (u.getPublicKey() != null) {
//            return repository.findByPublicKey(u.getPublicKey())
//                    .orElseThrow(() -> new InvalidUserExeption("User not found"));
//        }
//        if (u.getName() != null) {
//            return repository.findByPublicKey(u.getPublicKey())
//                    .orElseThrow(() -> new InvalidUserExeption("User not found"));
//        }
        throw new InvalidUserExeption("no username or public key provided");
    }

    @GetMapping("/users/name/{name}")
    User fromName(@PathVariable String name) {
        List<User> res = repository.findByName(name);
        if(res.size() == 1) {
            return res.get(0);
        }
        return null;
//        return repository.findByName(name)
//                .orElseThrow(() -> new InvalidUserExeption("User not found"));
    }
    @PostMapping("/newUser")
    User newUser(@RequestBody User newUser) {
        //Check if user already exists
        //This function returns an optional user.
        // Will be null if there is a user already found with same email or public key.
        if (newUser.shouldAddTo(repository)) {
            User response = repository.save(newUser);
            //Save backup
            Backup<User> b = new Backup<>(Backup.FileName.users);
            b.backup(repository.findAll());
            return response;
        } else {
            return null;
        }
    }
}
