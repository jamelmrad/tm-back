package com.telcotek.userservice.controller;

import com.telcotek.userservice.dto.TeamDto;
import com.telcotek.userservice.model.Team;
import com.telcotek.userservice.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Team> addTeam(@RequestBody TeamDto teamDto) {
            Team team = teamService.createTeam(teamDto);
            return new ResponseEntity<>(team,HttpStatus.OK);
    }

    @GetMapping("/mission/{mission-id}")
    @ResponseBody
    public List<Team> retrieveAllTeamsByMission(@PathVariable("mission-id") Long missionId) {
        return teamService.getAllByMissionId(missionId);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Team> retrieveAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping("/{team-id}/assign/{mission-id}")
    public ResponseEntity<?> assignMission(@PathVariable("mission-id") Long missionId,@RequestBody List<Team> teams) {
        try {
            teamService.assignMissionToTeam(missionId,teams);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
