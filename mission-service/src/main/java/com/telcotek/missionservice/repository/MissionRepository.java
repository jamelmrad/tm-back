package com.telcotek.missionservice.repository;

import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByStatus(Status status);

    List<Mission> findAllByOrderByProgressAsc();
    List<Mission> findAllByOrderByProgressDesc();

    List<Mission> findAllByOrderByPriorityLevelAsc();
    List<Mission> findAllByOrderByPriorityLevelDesc();

    List<Mission> findAllByOrderByStartTimeAsc();
    List<Mission> findAllByOrderByStartTimeDesc();

    List<Mission> findAllByOrderByEndedTimeAsc();
    List<Mission> findAllByOrderByEndedTimeDesc();

    List<Mission> findAllByApproved(Boolean b);

}