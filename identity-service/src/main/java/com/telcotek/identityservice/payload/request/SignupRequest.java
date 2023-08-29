package com.telcotek.identityservice.payload.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
