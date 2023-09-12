package com.telcotek.authenticationservice.security.services;

import com.telcotek.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.telcotek.userservice.model.User;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  RestTemplate restTemplate;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Call user-service to verify user existence
    String userServiceUrl = "http://localhost:8084/api/users/get";

    // Build the URL with request parameters
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
            .queryParam("email",email);

    Optional<User> user = Optional.ofNullable(restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET, null,
            User.class
    ).getBody());

    return UserDetailsImpl.build(user);
  }

}
