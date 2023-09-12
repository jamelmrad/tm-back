package com.telcotek.userservice.controller;

import com.telcotek.userservice.dto.UserDto;
import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import com.telcotek.userservice.model.User;
import com.telcotek.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/new")
    public void save(@RequestBody User user) {
            userService.saveUser(user);
    }

    @GetMapping("/role")
    @ResponseBody
    public Role getRoleByName(@RequestParam("role") ERole role) {
        return userService.getByName(role);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users-available")
    public ResponseEntity<List<User>> getAllAvailableUsers() {
        try {
            List<User> availableUsers = userService.retrieveAllAvailableUsers();

            if (availableUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(availableUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    @ResponseBody
    public String getUserByEmail(@RequestParam("email") String email) {
        return userService.retrieveUserFullName(email);
    }

    @GetMapping("/verify")
    @ResponseBody
    public Boolean isEmailVerified(@RequestParam("email") String email) {
        return userService.emailVerified(email);
    }

    @GetMapping("/get")
    @ResponseBody
    public User get(@RequestParam("email") String email) {
        return userService.retrieveUserByEmail(email).get();
    }

    @GetMapping("/existence")
    @ResponseBody
    public Boolean existence(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @PutMapping("/update")
    public void availableUser(@RequestParam("email") String email) {
        userService.setUserAvailable(email);
    }
}
