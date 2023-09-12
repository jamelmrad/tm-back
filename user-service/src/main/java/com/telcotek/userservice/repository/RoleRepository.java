package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole name);
}
