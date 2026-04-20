package com.twitter.app.repositories;

import com.twitter.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAuth0Sub(String auth0Sub);
    boolean existsByUsername(String username);
}