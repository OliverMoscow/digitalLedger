package com.cc.digitalLedger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPublicKey(String PUBLIC_KEY);
    User findByName(String NAME);

}
