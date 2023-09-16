package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.Admin;
import com.telcotek.userservice.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {

    @Query(value = "SELECT * FROM t_users WHERE id = :userId", nativeQuery = true)
    SuperAdmin get(@Param("userId")Long id);
}