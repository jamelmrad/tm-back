package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {
    List<SuperAdmin> findAllById(Long id);
}