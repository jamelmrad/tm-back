package com.telcotek.userservice.repository;

import com.telcotek.userservice.model.Admin;
import com.telcotek.userservice.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByMissionId(Long missionId);

    Optional<Team> findByAdminsContains(Admin admin);
}