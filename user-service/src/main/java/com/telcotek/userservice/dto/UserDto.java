package com.telcotek.userservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
