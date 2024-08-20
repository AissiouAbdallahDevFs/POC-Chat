package com.POC.Chat.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.POC.Chat.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	 Optional<User> findByEmail(String email);

    Iterable<User> findByIsConnected(boolean b);

    User findByUsername(String username);
}

