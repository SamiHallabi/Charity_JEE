package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
