package com.telcotek.authenticationservice.controllers;

import com.telcotek.authenticationservice.payload.request.*;
import com.telcotek.authenticationservice.payload.response.*;
import com.telcotek.authenticationservice.security.jwt.JwtUtils;
import com.telcotek.authenticationservice.security.services.UserDetailsImpl;
import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import com.telcotek.userservice.model.User;
import com.telcotek.userservice.repository.RoleRepository;
import com.telcotek.userservice.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
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

        // Define the URL of the endpoint you want to send the PUT request to
        String url = "http://localhost:8084/api/users/setOnline";

        // Define the request parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create a request body with the parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", loginRequest.getEmail());

        // Create an HttpEntity with the request body and headers
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Perform the PUT request using RestTemplate
        restTemplate.put(url, requestEntity);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws InterruptedException {

        // Call user-service to verify user existence
        String userExistenceUrl = "http://localhost:8084/api/users/existence";

        // Build the URL with request parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userExistenceUrl)
                .queryParam("email",signUpRequest.getEmail());

        Boolean exist = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET, null,
                Boolean.class
        ).getBody();

        if (exist) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account

        User user = new User(
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        if (signUpRequest.getPassword() == "" || signUpRequest.getPassword() == null) {
            user.setPassword("changeme");
        }
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            // Call user-service to verify user existence
            String userServiceUrl = "http://localhost:8084/api/users/existence";

            // Build the URL with request parameters
            UriComponentsBuilder secondBuilder = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                    .queryParam("role",ERole.ROLE_CLIENT);

            Role userRole = restTemplate.exchange(
                    secondBuilder.toUriString(),
                    HttpMethod.GET, null,
                    Role.class).getBody();
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "mod":
                        // Call user-service to verify user existence
                        String userServiceUrl = "http://localhost:8084/api/users/role";

                        // Build the URL with request parameters
                        UriComponentsBuilder secondBuilder = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                                .queryParam("role",ERole.ROLE_MODERATOR);

                        Role modRole = restTemplate.exchange(
                                secondBuilder.toUriString(),
                                HttpMethod.GET, null,
                                Role.class).getBody();
                        roles.add(modRole);

                        break;
                    default:
                        // Call user-service to verify user existence
                        String serServiceUrl = "http://localhost:8084/api/users/role";

                        // Build the URL with request parameters
                        UriComponentsBuilder thirdBuilder = UriComponentsBuilder.fromHttpUrl(serServiceUrl)
                                .queryParam("role",ERole.ROLE_CLIENT);

                        Role userRole = restTemplate.exchange(
                                thirdBuilder.toUriString(),
                                HttpMethod.GET, null,
                                Role.class).getBody();
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        // Call user-service to save user
        String userSaveUrl = "http://localhost:8084/api/users/new";

        restTemplate.postForEntity(userSaveUrl,user,User.class);

        Thread.sleep(5000);

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

        // Perform the POST request using RestTemplate
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8085/send-email", requestEntity, String.class);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        String url = "http://localhost:8084/api/users/setOffline";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            // Define the request parameters
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Create a request body with the parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("email", authentication.getName());

            // Create an HttpEntity with the request body and headers
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            // Perform the PUT request using RestTemplate
            restTemplate.put(url, requestEntity);
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping()
    @ResponseBody
    public String get() {
        String userServiceUrl = "http://localhost:8084/api/users";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            // authentication.getName(); // This gets the email of the authenticated user

            // Build the URL with request parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                    .queryParam("email", authentication.getName());

            // Make the GET request to the user service
            //return restTemplate.getForObject(builder.toUriString(), String.class);
            return authentication.getName();

        }
        return null;
    }
}

