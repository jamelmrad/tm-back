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
        Integer members = teamDto.getAdmins().size() + teamDto.getOfficers().size() +1;

        Team team = Team.builder()
                .name(teamDto.getName())
                .numberOfTeamMembers(members)
                .missionId(teamDto.getMissionId())
                .officers(teamDto.getOfficers())
                .admins(teamDto.getAdmins())
                .superAdmin(teamDto.getSuperAdmin())
                .build();


        for (Officer officer:team.getOfficers()) {
            officer.setTeam(team);
            officer.setAvailable(Boolean.FALSE);
        }
        for (Admin admin:team.getAdmins()) {
            admin.setTeam(team);
            admin.setAvailable(Boolean.FALSE);
        }
        teamRepository.save(team);

        SuperAdmin superAdmin = team.getSuperAdmin();
        superAdmin.setTeam(team);
        superAdmin.setAvailable(Boolean.FALSE);

        superAdminRepository.save(superAdmin);

        officerRepository.saveAll(team.getOfficers());

        adminRepository.saveAll(team.getAdmins());

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
