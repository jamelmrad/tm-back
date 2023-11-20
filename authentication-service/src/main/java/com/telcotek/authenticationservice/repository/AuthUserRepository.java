package com.telcotek.authenticationservice.repository;

import com.telcotek.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
