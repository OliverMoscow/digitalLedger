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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(publicKey, user.publicKey);
    }
}
