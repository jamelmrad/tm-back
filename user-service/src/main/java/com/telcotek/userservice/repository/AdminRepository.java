package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.Admin;
import com.telcotek.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query(value = "SELECT * FROM t_users WHERE id = :userId", nativeQuery = true)
    Admin get(@Param("userId")Long id);
}