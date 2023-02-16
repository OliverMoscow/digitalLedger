package com.cc.digitalLedger;


import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PUBLIC_KEY")
    private String publicKey;

    public User(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
