package com.telcotek.userservice.service;

import com.opencsv.exceptions.CsvException;
import com.telcotek.userservice.helper.CSVReader;
import com.telcotek.userservice.model.ERole;
import com.telcotek.userservice.model.Role;
import com.telcotek.userservice.model.User;
import com.telcotek.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CSVService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    public void save(MultipartFile file) {
        try {
            List<String[]> records = CSVReader.readCsv(file);
            records.remove(0);
            List<User> usersList = new ArrayList<>();

            Set<Role> roles = new HashSet<>();
            Role role = userService.getByName(ERole.ROLE_CLIENT);
            roles.add(role);

            for(String[] csvRecord : records) {
                User user = User.builder()
                        .Id(Long.parseLong(csvRecord[0]))
                        .firstname(csvRecord[1])
                        .lastname(csvRecord[2])
                        .phoneNumber(Long.parseLong(csvRecord[3]))
                        .email(csvRecord[4])
                        .password("changeMe")
                        .available(Boolean.FALSE)
                        .connected(Boolean.FALSE)
                        .emailVerified(Boolean.FALSE)
                        .roles(roles)
                        .build();
                usersList.add(user);

                userRepository.save(user);
                userService.notifyUserListUpdate();

                // send email for each user : code below

                // Define the request parameters
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                // Create a request body with the parameters
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("destination", user.getEmail());
                params.add("subject", "Account Verification");
                params.add("random-link-identifier", UUID.randomUUID().toString());

                // Create an HttpEntity with the request body and headers
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

                // Perform the POST request using RestTemplate
                restTemplate.postForEntity("http://localhost:8086/send-email", requestEntity, String.class);

            }
            //userRepository.saveAll(usersList);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
