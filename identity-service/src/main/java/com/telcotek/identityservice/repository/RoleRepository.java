package com.telcotek.identityservice.repository;


import com.telcotek.identityservice.models.ERole;
import com.telcotek.identityservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(ERole name);
}
