package com.telcotek.userservice.service;

import com.opencsv.exceptions.CsvException;
import com.telcotek.userservice.helper.CSVReader;
import com.telcotek.userservice.model.User;
import com.telcotek.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    UserRepository userRepository;

    public void save(MultipartFile file) {
        try {
            List<String[]> records = CSVReader.readCsv(file);
            records.remove(0);
            List<User> usersList = new ArrayList<>();
            for(String[] csvRecord : records) {
                User user = User.builder()
                        .Id(Long.parseLong(csvRecord[0]))
                        .firstname(csvRecord[1])
                        .lastname(csvRecord[2])
                        .phoneNumber(Long.parseLong(csvRecord[3]))
                        .email(csvRecord[4])
                        .available(Boolean.FALSE)
                        .connected(Boolean.FALSE)
                        .emailVerified(Boolean.FALSE)
                        .build();
                usersList.add(user);
            }
            userRepository.saveAll(usersList);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
