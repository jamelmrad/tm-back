package com.telcotek.missionservice.controller;

import com.telcotek.missionservice.dto.MissionRequest;
import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.service.MissionService;
import com.telcotek.missionservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/missions")
public class MissionController {

    @Autowired
    MissionService missionService;

    @Autowired
    TaskService taskService;


    // define similar endpoints in a similar way : e.g go this for all the methods below
    @GetMapping("/mission/secure-endpoint")
    public ResponseEntity<String> secureEndpoint() {
        // Retrieve authenticated user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.ok("Hello, " + username + "! This is a secure endpoint.");
    }

    @PostMapping("/add")
    public ResponseEntity<Mission> createMission(@RequestBody MissionRequest missionRequest) {
        try {
            Mission mission = missionService.createMission(missionRequest);

            return new ResponseEntity<>(mission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @PostMapping("/{id}/add-tasks")
    public ResponseEntity<Mission> assignTasks(@RequestBody List<Task> tasks ,@PathVariable("id") Long missionId) {
        try {
            Mission mission = missionService.assignTasksToMission(missionId,tasks);

            if (tasks.equals(null) || mission.equals(null)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<HttpStatus> approveMission(@PathVariable("id") Long missionId) {
        try {
            missionService.approveMission(missionId);

            return new ResponseEntity<>(HttpStatus.valueOf("Mission approved !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Mission> getMission(@PathVariable("id") Long missionId) {
        try {
            Mission mission = missionService.retrieveMissionById(missionId);

            return new ResponseEntity<Mission>(mission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Mission>> getAllMissions() {
        try {
            List<Mission> missions = missionService.retrieveAllMissions();

            if (missions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/by_status")
    @ResponseBody
    public ResponseEntity<List<Mission>> getAllMissionsByStatus(@RequestParam("status")Status status) {
        try {
            List<Mission> missions = missionService.retrieveAllMissionsByStatus(status);

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/filterBy_progress_ASC")
    @ResponseBody
    public ResponseEntity<List<Mission>> allProgressAsc() {
        try {
            List<Mission> missions = missionService.retrieveMissionsByProgressAsc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/filterBy_progress_DESC")
    @ResponseBody
    public ResponseEntity<List<Mission>> allProgressDesc() {
        try {
            List<Mission> missions = missionService.retrieveMissionsByProgressDesc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/filterBy_priority_ASC")
    @ResponseBody
    public ResponseEntity<List<Mission>> allPriorityAsc() {
        try {
            List<Mission> missions = missionService.retrieveMissionsByPriorityAsc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/filterBy_priority_DESC")
    @ResponseBody
    public ResponseEntity<List<Mission>> allPriorityDesc() {
        try {
            List<Mission> missions = missionService.retrieveMissionsByPriorityDesc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/approved")
    @ResponseBody
    public ResponseEntity<List<Mission>> allApproved() {
        try {
            List<Mission> missions = missionService.retrieveMissionsApproved();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/non_approved")
    @ResponseBody
    public ResponseEntity<List<Mission>> allNonApproved() {
        try {
            List<Mission> missions = missionService.retrieveMissionsNonApproved();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/st_asc")
    @ResponseBody
    public ResponseEntity<List<Mission>> allSTAsc() {
        try {
            List<Mission> missions = missionService.retrieveAllByStartTimeAsc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/st_desc")
    @ResponseBody
    public ResponseEntity<List<Mission>> allSTDesc() {
        try {
            List<Mission> missions = missionService.retrieveAllByStartTimeDesc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/et_asc")
    @ResponseBody
    public ResponseEntity<List<Mission>> allETAsc() {
        try {
            List<Mission> missions = missionService.retrieveAllByEndedTimeAsc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/all/et_desc")
    @ResponseBody
    public ResponseEntity<List<Mission>> allETDesc() {
        try {
            List<Mission> missions = missionService.retrieveAllByEndedTimeDesc();

            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

    @GetMapping("/percentage/status")
    @ResponseBody
    public ResponseEntity<Object> percentageByStatus() {
        return new ResponseEntity<>(missionService.statusPercentage(), HttpStatus.OK);
    }

    @DeleteMapping ("/{id}/remove")
    public ResponseEntity<HttpStatus> deleteMission(@PathVariable("id") Long missionId) {
        try {
            missionService.deleteMission(missionId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //

}
