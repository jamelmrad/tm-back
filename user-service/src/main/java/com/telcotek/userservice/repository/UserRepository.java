package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByAvailableTrue();
    Boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM t_users WHERE email = :userEmail", nativeQuery = true)
    List<User> findMemberByEmail(@Param("userEmail")String email);

    @Query(value = "SELECT * FROM t_users WHERE id = :userId", nativeQuery = true)
    User get(@Param("userId")Long id);

    @Query(value = "SELECT * FROM t_users WHERE member_type = :mt AND email = :userEmail ", nativeQuery = true)
    List<User> getAllByMemberType(@Param("mt") String memberType, @Param("userEmail") String userEmail);

    @Query(value = "SELECT * FROM t_users WHERE member_type = 'User'", nativeQuery = true)
    List<User> findAllMembers();

    @Query(value = "SELECT * FROM t_users WHERE member_type = 'User' AND connected = true", nativeQuery = true)
    List<User> onlineMembers();
}
