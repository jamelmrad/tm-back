package com.telcotek.missionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcotek.missionservice.dto.MissionRequest;
import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MissionService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    TaskService taskService;
    @Autowired

    private SimpMessagingTemplate messagingTemplate;

    private final static String MISSION_NOT_FOUND_MSG = "Mission not found";


    /** REAL-TIME UPDATES */
    public void notifyMissionListUpdate() {
        messagingTemplate.convertAndSend("/task-management/missions", retrieveAllMissions());
    }

    public void notifyMissionDetailsUpdate(Long missionId) {
        messagingTemplate.convertAndSend("/task-management/missions/" + missionId , retrieveMissionById(missionId));
    }

    /** CREATE FUNCTIONS */
    public Mission createMission(MissionRequest missionRequest) {

        String chatServiceUrl= "http://localhost:8083/api/chat/new-chat";

        Mission mission = Mission.builder()
                .title(missionRequest.getTitle())
                .priorityLevel(missionRequest.getPriorityLevel())
                .build();
        mission.setStatus(Status.TODO);
        mission.setProgress(0.0);
        mission.setApproved(Boolean.FALSE);
        mission.setStartTime(missionRequest.getStartTime());
        mission.setEndedTime(missionRequest.getEndedTime());
        mission.setTasks(new ArrayList<>());

        missionRepository.save(mission);

        notifyMissionListUpdate();

        // Build the URL with request parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(chatServiceUrl)
                        .queryParam("mission_id", mission.getId())
                                .queryParam("mission_title", mission.getTitle());

        // Make the POST request to the chat service
        restTemplate.postForObject(builder.toUriString(), null, String.class);

        return mission;
    }

     public Mission assignTasksToMission(Long missionId, List<Task> tasks) {
         Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));

         for (Task t:tasks) {
             mission.addTask(t);
             t.setMission(mission);
             taskService.initializeTask(t);

         }
         LocalDateTime time = LocalDateTime.now();
         mission.setStartTime(time);

         missionRepository.save(mission);

         notifyMissionDetailsUpdate(missionId);
         notifyMissionListUpdate();

         return mission;
     }

    /** UPDATE FUNCTIONS */
    public void approveMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        mission.setApproved(Boolean.TRUE);
        LocalDateTime time = LocalDateTime.now();
        mission.setEndedTime(time);
        missionRepository.save(mission);
        notifyMissionDetailsUpdate(missionId);
        notifyMissionListUpdate();
    }

    public void setMissionProgress(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        List<Task> tasks = mission.getTasks();
        if (!tasks.isEmpty()) {
            double sum = 0;
            for(Task t:tasks) {
                // Task progress can be O or 100 only
                sum = sum + t.getProgress();
            }
            mission.setProgress(sum/tasks.size());
            missionRepository.save(mission);
            notifyMissionDetailsUpdate(missionId);
            notifyMissionListUpdate();
        } else  {
            return;
        }
    }

    public void changeMissionStatus(Long missionId) {
        // Triggers every
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        double progress = getMissionProgress(missionId);
        if (progress == 100) {
            mission.setStatus(Status.DONE);
        }
        if (progress < 100 && progress > 0 ) {
            mission.setStatus(Status.DOING);
        }
        if (progress == 0) {
            mission.setStatus(Status.TODO);
        }
        missionRepository.save(mission);
        notifyMissionDetailsUpdate(missionId);
        notifyMissionListUpdate();
    }

    /** RETRIEVE FUNCTIONS */
    public Object statusPercentage() {

        long totalMissions = missionRepository.findAll().size();
        long todoMissions = retrieveAllMissionsByStatus(Status.TODO).size();
        long doingMissions = retrieveAllMissionsByStatus(Status.DOING).size();
        long doneMissions = retrieveAllMissionsByStatus(Status.DONE).size();

        if (totalMissions == 0) {
            return null;
        }

        long todoPercentage = (todoMissions * 100) / totalMissions;
        long doingPercentage = (doingMissions * 100) / totalMissions;
        long donePercentage = (doneMissions * 100) / totalMissions;

        Map<String, Object> todoJson = Map.of("Status", Status.TODO, "percentage", todoPercentage);
        Map<String, Object> doingJson = Map.of("Status", Status.DOING, "percentage", doingPercentage);
        Map<String, Object> doneJson = Map.of("Status", Status.DONE, "percentage", donePercentage);

        Map<String,Object>[] result = new Map[]{todoJson,doingJson,doneJson};

        return result;
    }

    public double getMissionProgress(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        List<Task> tasks = mission.getTasks();
        if (!tasks.isEmpty()) {
            double sum = 0;
            for(Task t:tasks) {
                // Task progress can be O or 100 only
                sum = sum + t.getProgress();
            }
            mission.setProgress(sum/tasks.size());
            missionRepository.save(mission);
            return sum / tasks.size();
        } else  {
            return 0;
        }
    }

    public Mission retrieveMissionById(Long missionId) {
        return missionRepository.findById(missionId).get();
    }

    public List<Mission> retrieveAllMissions() {
        return missionRepository.findAll();
    }

    public List<Mission> retrieveAllMissionsByStatus(Status status) {
        switch (status) {
            case TODO -> {return missionRepository.findByStatus(Status.TODO);}
            case DOING -> {return missionRepository.findByStatus(Status.DOING);}
            case DONE -> {return missionRepository.findByStatus(Status.DONE);}
        }
     return null;
    }

    public List<Mission> retrieveMissionsByProgressAsc() {
        return missionRepository.findAllByOrderByProgressAsc();
    }
    public List<Mission> retrieveMissionsByProgressDesc() {
        return missionRepository.findAllByOrderByProgressDesc();
    }


    public List<Mission> retrieveMissionsByPriorityAsc() {
        return missionRepository.findAllByOrderByPriorityLevelAsc();
    }
    public List<Mission> retrieveMissionsByPriorityDesc() {
        return missionRepository.findAllByOrderByPriorityLevelDesc();
    }

    public List<Mission> retrieveMissionsApproved() {
        return  missionRepository.findAllByApproved(Boolean.TRUE);
    }
    public List<Mission> retrieveMissionsNonApproved() {
        return  missionRepository.findAllByApproved(Boolean.FALSE);
    }

    public List<Mission> retrieveAllByStartTimeAsc() {
        return missionRepository.findAllByOrderByStartTimeAsc();
    }
    public List<Mission> retrieveAllByStartTimeDesc() {
        return missionRepository.findAllByOrderByStartTimeDesc();
    }

    public List<Mission> retrieveAllByEndedTimeAsc() {
        return missionRepository.findAllByOrderByEndedTimeAsc();
    }
    public List<Mission> retrieveAllByEndedTimeDesc() {
        return missionRepository.findAllByOrderByEndedTimeDesc();
    }

    public List<Task> retrieveAllTasksInMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        return mission.getTasks();
    }

    public List<Mission> retrieveAllMissionsFromIds(Long userId) {

        /** Get mission Ids from user-service */
        String apiUrl = "http://localhost:8084/api/users/" + userId + "/mission-ids";

        // Perform the GET request using RestTemplate
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {}
        );

        /** Retrieve missions */
        List<Mission> missions = new ArrayList<>();
        for (Long id:response.getBody()) {
            missions.add(missionRepository.findById(id).get());
        }
        return missions;
    }

    /** DELETE FUNCTIONS */
    public void deleteMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException(MISSION_NOT_FOUND_MSG));
        missionRepository.delete(mission);
        notifyMissionListUpdate();
    }
}
