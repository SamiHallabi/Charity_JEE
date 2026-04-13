package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);


}
