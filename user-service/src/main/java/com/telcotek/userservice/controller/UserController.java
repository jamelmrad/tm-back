package com.telcotek.userservice.controller;

import com.telcotek.userservice.model.*;
import com.telcotek.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
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

    @GetMapping("/all")
    @ResponseBody
    public List<User> getAllUsers() {
            return userService.getAll();
    }

    @GetMapping("/online")
    @ResponseBody
    public List<User> getAllOnline() {
        return userService.getAllOnlineUsers();
    }

    @GetMapping("/{id}/mission-ids")
    @ResponseBody
    public List<Long> getAllUserMissions(@PathVariable("id") Long id) {
            return userService.retrieveUserMissions(id);
    }

    @GetMapping("/users-available")
    public List<User> getAllAvailableUsers() {
            return userService.retrieveAllAvailableUsers();
    }

    @GetMapping()
    @ResponseBody
    public String getUserByEmail(@RequestParam("email") String email) {
        return userService.retrieveUserFullName(email);
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Long id) {
        return userService.retrieveUserById(id);
    }

    @GetMapping("/verify")
    @ResponseBody
    public Boolean isEmailVerified(@RequestParam("email") String email) {
        return userService.emailVerified(email);
    }

    @GetMapping("/get")
    @ResponseBody
    public User get(@RequestParam("email") String email) {
        return userService.retrieveUserByEmail(email);
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

    @PutMapping("/setPassword")
    public void setUserPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        userService.setPassword(email,password);
    }

    @PutMapping("/setOnline")
    public void setUserOnline(@RequestParam("email") String email) {
        userService.setOnline(email);
    }

    @PutMapping("/update-user/{id}")
    public void update(@RequestBody User user,@PathVariable("id") Long id) {
        userService.update(user,id);
    }

    @PutMapping("/setOffline")
    public void setUserOffline(@RequestParam("email") String email) {
        userService.setOffline(email);
    }

    @PutMapping("/verify-account")
    public void verifyAccount(@RequestParam("email") String email) {
        userService.verifyUserAccount(email);
    }
}
