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
@DiscriminatorValue("superadmin")
public class SuperAdmin extends User{

    @OneToOne(mappedBy = "superAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Team team;

    public void setTeam(Team team) {
        this.team = team;
    }
}
