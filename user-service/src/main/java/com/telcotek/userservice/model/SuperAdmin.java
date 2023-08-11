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
@Table(name = "t_superadmins")
public class SuperAdmin extends User{

    @OneToOne
    @JoinColumn(name = "team_id",nullable = false)
            @JsonIgnore
    Team team;

    public void setTeam(Team team) {
        this.team = team;
    }
}
