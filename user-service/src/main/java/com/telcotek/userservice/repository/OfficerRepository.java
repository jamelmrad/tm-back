package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {

    @Query(value = "SELECT * FROM t_users WHERE id = :userId", nativeQuery = true)
    Officer get(@Param("userId")Long id);
}