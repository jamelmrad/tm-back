package com.telcotek.userservice.service;

import com.telcotek.userservice.dto.UserDto;
import com.telcotek.userservice.model.*;
import com.telcotek.userservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OfficerRepository officerRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /** REAL-TIME UPDATES */
    public void notifyUserListUpdate() {
        messagingTemplate.convertAndSend("/task-management/users", getAll());
    }

    public void notifyUserDetailsUpdate(Long userId) {
        messagingTemplate.convertAndSend("/task-management/users/" + userId , retrieveUserById(userId));
    }

    public void saveUser(User user) {
        userRepository.save(user);
        notifyUserListUpdate();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getAllUsers() {
        return userRepository.findAllMembers();
    }

    public List<User> getAllOnlineUsers() {
        return userRepository.onlineMembers();
    }

    public List<User> retrieveAllAvailableUsers() {
        return userRepository.findAllByAvailableTrue();
    }

    public User retrieveUserById(Long userId) {
       // return userRepository.get(userId);
        return userRepository.findById(userId).get();
    }

    public User retrieveUserByEmail(String email) {
        return userRepository.findMemberByEmail(email).get(0);
    }

    public String retrieveUserFullName(String email) {
        User user = retrieveUserByEmail(email);
        return user.getFirstname() + " " + user.getLastname();
    }

    public void setTeamMembersUnavailable(List<? extends User> users) {
        for (User user:users) {
            User x = userRepository.getReferenceById(user.getId());
            x.setAvailable(Boolean.FALSE);
            userRepository.save(x);
            notifyUserDetailsUpdate(x.getId());
            notifyUserListUpdate();
        }
    }

    public void setTeamMemberAvailable(List<? extends User> users) {
        for (User user:users) {
            user.setAvailable(Boolean.TRUE);
            userRepository.save(user);
            notifyUserDetailsUpdate(user.getId());
            notifyUserListUpdate();
        }
    }

    public void setUserUnavailable(User user) {
        user.setAvailable(Boolean.FALSE);
        userRepository.save(user);
       // notifyUserDetailsUpdate(userId);
        notifyUserListUpdate();
    }

    public void setUserAvailable(String userEmail) {
        User user = retrieveUserByEmail(userEmail);
        user.setAvailable(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
        notifyUserListUpdate();
    }

    public Boolean emailVerified(String email) {
        User user = retrieveUserByEmail(email);
        return user.getEmailVerified();
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void update(User user,Long id) {
        if (userRepository.existsById(id)) {
            userRepository.save(user);
        }
    }

    public Role getByName(ERole name) {
        return roleRepository.findByName(name);
    }

    public void setOnline(String email) {
        User user = retrieveUserByEmail(email);
        user.setConnected(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
        notifyUserListUpdate();
    }

    public void setOffline(String email) {
        User user = retrieveUserByEmail(email);
        user.setConnected(Boolean.FALSE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
        notifyUserListUpdate();
    }

    public void setPassword(String email ,String password) {
        User user = retrieveUserByEmail(email);
        user.setPassword(password);
        user.setEmailVerified(Boolean.TRUE);
        user.setAvailable(Boolean.TRUE);
        userRepository.save(user);
        notifyUserListUpdate();
        notifyUserDetailsUpdate(user.getId());
    }

    public void verifyUserAccount(String email) {
        User user = retrieveUserByEmail(email);
        user.setEmailVerified(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
        notifyUserListUpdate();
    }

    public List<Long> retrieveUserMissions(Long userId) {

        User user = userRepository.get(userId);
        String userEmail = user.getEmail();

        List<User> admins = userRepository.getAllByMemberType("admin",userEmail);
        List<User> superAdmins = userRepository.getAllByMemberType("superadmin",userEmail);
        List<User> officers = userRepository.getAllByMemberType("officer",userEmail);

        List<Long> missionIds = new ArrayList<>();

        for (User admin:admins) {
            Admin x = adminRepository.get(admin.getId());
            missionIds.add(x.getTeam().getMissionId());
        }
        for (User superAdmin:superAdmins) {
            SuperAdmin x = superAdminRepository.get(superAdmin.getId());
            missionIds.add(x.getTeam().getMissionId());
        }
        for (User officer:officers) {
            Officer x = officerRepository.get(officer.getId());
            missionIds.add(x.getTeam().getMissionId());
        }

        return missionIds;
    }

}
