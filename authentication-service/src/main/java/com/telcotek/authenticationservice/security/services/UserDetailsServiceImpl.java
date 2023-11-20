package com.telcotek.authenticationservice.security.services;

import com.telcotek.authenticationservice.repository.AuthUserRepository;
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
  AuthUserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));

    return UserDetailsImpl.build(user);
  }

}
