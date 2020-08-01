package com.proximity.labs.qcounter.data.repositories;

import java.util.Optional;

import com.proximity.labs.qcounter.data.models.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    
    Optional<User> findByName(String name);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}