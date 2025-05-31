package com.ubu.p3.twentyonepilots.repository;

import com.ubu.p3.twentyonepilots.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
