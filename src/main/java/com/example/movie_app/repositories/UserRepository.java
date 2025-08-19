package com.example.movie_app.repositories;

import com.example.movie_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
