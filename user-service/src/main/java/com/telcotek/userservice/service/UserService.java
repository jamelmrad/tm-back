package com.telcotek.userservice.service;

import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import com.telcotek.userservice.model.User;
import com.telcotek.userservice.repository.RoleRepository;
import com.telcotek.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /** REAL-TIME UPDATES */
    public void notifyUserListUpdate() {
        messagingTemplate.convertAndSend("/task-management/users", getAllUsers());
    }

    public void notifyUserDetailsUpdate(Long userId) {
        messagingTemplate.convertAndSend("/task-management/users/" + userId , retrieveUserById(userId));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> retrieveAllAvailableUsers() {
        return userRepository.findAllByAvailableTrue();
    }

    public User retrieveUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public Optional<User> retrieveUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String retrieveUserFullName(String email) {
        User user = userRepository.findByEmail(email).get();
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
        User user = userRepository.findByEmail(userEmail).get();
        user.setAvailable(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
        notifyUserListUpdate();
    }

    public Boolean emailVerified(String email) {
        User user = userRepository.findByEmail(email).get();
        return user.getEmailVerified();
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Role getByName(ERole name) {
        return roleRepository.findByName(name);
    }

    public void setOnline(String email) {
        User user = userRepository.findByEmail(email).get();
        user.setConnected(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
    }

    public void setOffline(String email) {
        User user = userRepository.findByEmail(email).get();
        user.setConnected(Boolean.FALSE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
    }

    public void setPassword(String email ,String password) {
        User user = userRepository.findByEmail(email).get();
        user.setPassword(password);
        user.setEmailVerified(Boolean.TRUE);
        user.setAvailable(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(user.getId());
    }

}
