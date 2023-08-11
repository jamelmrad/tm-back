package com.telcotek.userservice.service;

import com.telcotek.userservice.model.User;
import com.telcotek.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /** REAL-TIME UPDATES */
    public void notifyUserListUpdate() {
        messagingTemplate.convertAndSend("/task-management/users", getAllUsers());
    }

    public void notifyUserDetailsUpdate(Long userId) {
        messagingTemplate.convertAndSend("/task-management/users/" + userId , retrieveUserById(userId));
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

    public void setTeamMembersUnavailable(List<? extends User> users) {
        for (User user:users) {
            User x = userRepository.getReferenceById(user.getUserId());
            x.setAvailable(Boolean.FALSE);
            userRepository.save(x);
            notifyUserDetailsUpdate(x.getUserId());
            notifyUserListUpdate();
        }
    }

    public void setTeamMemberAvailable(List<? extends User> users) {
        for (User user:users) {
            user.setAvailable(Boolean.TRUE);
            userRepository.save(user);
            notifyUserDetailsUpdate(user.getUserId());
            notifyUserListUpdate();
        }
    }

    public void setUserUnavailable(User user) {
        user.setAvailable(Boolean.FALSE);
        userRepository.save(user);
       // notifyUserDetailsUpdate(userId);
        notifyUserListUpdate();
    }

    public void setUserAvailable(Long userId) {
        User user = retrieveUserById(userId);
        user.setAvailable(Boolean.TRUE);
        userRepository.save(user);
        notifyUserDetailsUpdate(userId);
        notifyUserListUpdate();
    }

}
