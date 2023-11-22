package com.telcotek.authenticationservice.controllers;

import com.telcotek.authenticationservice.payload.request.*;
import com.telcotek.authenticationservice.payload.response.*;
import com.telcotek.authenticationservice.repository.AuthUserRepository;
import com.telcotek.authenticationservice.repository.RoleAuthRepository;
import com.telcotek.authenticationservice.security.jwt.JwtUtils;
import com.telcotek.authenticationservice.security.services.UserDetailsImpl;
import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import com.telcotek.userservice.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    RoleAuthRepository roleAuthRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = authUserRepository.findByEmail(loginRequest.getEmail());
        user.setConnected(Boolean.TRUE);
        authUserRepository.save(user);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping()
    public ResponseEntity<?> initiateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String encodedPassword = encoder.encode(loginRequest.getPassword());

        String apiUrl = "http://localhost:8084/api/users/setPassword";

        // Define the request parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create a request body with the parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", loginRequest.getEmail());
        params.add("password", encodedPassword);

        // Create an HttpEntity with the request body and headers
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Perform the POST request using RestTemplate
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);

        return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest ) {

        Boolean exist = authUserRepository.existsByEmail(signUpRequest.getEmail());

        if (exist) {
            return ResponseEntity.ok().body(new MessageResponse("Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()));
        if (signUpRequest.getPassword() == "" || signUpRequest.getPassword() == null) {
            user.setPassword("changeme");
        }
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role r = new Role();
            r.setName(ERole.ROLE_CLIENT);
            roles.add(r);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "mod":
                        roles.add(roleAuthRepository.findByName(ERole.ROLE_MODERATOR));

                        break;
                    default:
                        roles.add(roleAuthRepository.findByName(ERole.ROLE_CLIENT));
                }
            });
        }

        user.setRoles(roles);

        authUserRepository.save(user);

        // Define the request parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create a request body with the parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("destination", user.getEmail());
        params.add("subject", "Account Verification");
        params.add("random-link-identifier", UUID.randomUUID().toString());

        // Create an HttpEntity with the request body and headers
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        if (signUpRequest.getRole().contains("mod")){
            // Perform the POST request using RestTemplate
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8085/api/mailing/mod-verif", requestEntity, String.class);
        }else{
            // Perform the POST request using RestTemplate
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8085/api/mailing/send-email", requestEntity, String.class);
        }

        return ResponseEntity.ok(new MessageResponse("You are registered successfully! A verification email has been sent to " + signUpRequest.getEmail()));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = authUserRepository.findByEmail(authentication.getName());

        if (authentication != null) {
            user.setConnected(Boolean.FALSE);
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}

