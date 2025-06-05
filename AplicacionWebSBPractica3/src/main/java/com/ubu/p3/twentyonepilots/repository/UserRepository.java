package com.ubu.p3.twentyonepilots.repository;

import com.ubu.p3.twentyonepilots.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    //busca usuario por nombre de login único
    Optional<User> findByUsername(String username);
}
