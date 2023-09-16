package com.telcotek.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("admin")
public class Admin extends User{

    @ManyToOne
    @JoinColumn(name = "team_id")
            @JsonIgnore
    Team team;

    public Admin(User user) {
        super();
        this.setFirstname(user.getFirstname());
        this.setLastname(user.getLastname());
        this.setEmail(user.getEmail());
        this.setAvailable(Boolean.FALSE);
        this.setConnected(user.getConnected());
        this.setPassword(user.getPassword());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setRoles(user.getRoles());
        this.setEmailVerified(user.getEmailVerified());
    }
}
