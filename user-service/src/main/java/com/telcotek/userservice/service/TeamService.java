package com.telcotek.userservice.service;

import com.telcotek.userservice.dto.TeamDto;
import com.telcotek.userservice.model.*;
import com.telcotek.userservice.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OfficerRepository officerRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    /** REAL-TIME UPDATES */
    public void notifyTeamListUpdate() {
        messagingTemplate.convertAndSend("/task-management/teams", getAllTeams());
    }

    public void notifyTeamListUpdateByMissionId(Long missionId) {
        messagingTemplate.convertAndSend("/task-management/missions/"+missionId+"/teams", getAllByMissionId(missionId));
    }

    public void notifyTeamDetailsUpdate(Long teamId) {
        messagingTemplate.convertAndSend("/task-management/teams/" + teamId , getTeamById(teamId));
    }

    /** CREATE FUNCTIONS */
    public Team createTeam(TeamDto teamDto) {
        Integer members = teamDto.getAdmins().size() + teamDto.getOfficers().size() + 1;

        Team team = Team.builder()
                .name(teamDto.getName())
                .numberOfTeamMembers(members)
                .missionId(teamDto.getMissionId())
                .build();

        List<Officer> officerList = new ArrayList<>();
        for (User user:teamDto.getOfficers()){
            Officer officer = new Officer(user);
            officer.setTeam(team);
            officerList.add(officer);
        }
        officerRepository.saveAll(officerList);
        team.setOfficers(officerList);

        List<Admin> adminList = new ArrayList<>();
        for (User user:teamDto.getAdmins()){
            Admin admin = new Admin(user);
            admin.setTeam(team);
            adminList.add(admin);
        }
        adminRepository.saveAll(adminList);
        team.setAdmins(adminList);

        SuperAdmin superAdmin = new SuperAdmin(teamDto.getSuperAdmin());
        superAdmin.setTeam(team);team.setSuperAdmin(superAdmin);
        superAdminRepository.save(superAdmin);


        teamRepository.save(team);
        return team;
    }

    /** UPDATE FUNCTIONS */
    public void assignMissionToTeam(Long missionId, List<Team> teams) {
        for (Team team:teams) {
            team.setMissionId(missionId);
            teamRepository.save(team);
        }
    }

    /** RETRIEVE FUNCTIONS */
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).get();
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getAllByMissionId(Long missionId) {
        return teamRepository.findAllByMissionId(missionId);
    }

    /** DELETE FUNCTIONS */
    public String delete(List<Team> teams) {
        teamRepository.deleteAll(teams);
        return "Mission dismissed";
    }
}
