package com.telcotek.userservice.controller;

import com.telcotek.userservice.model.User;
import com.telcotek.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserWebSocketController {

    @Autowired
    UserService userService;

    @MessageMapping("/users")
    @SendTo("/task-management/missions")
    public List<User> getUserListUpdate() {
        return userService.getAllUsers();
    }

    @MessageMapping("/users/{userId}")
    @SendTo("/task-management/users/{userId}")
    public User getUserUpdate(@DestinationVariable Long userId) {
        return userService.retrieveUserById(userId);
    }
}
