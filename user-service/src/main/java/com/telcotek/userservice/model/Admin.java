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
@Table(name = "t_admins")
public class Admin extends User{

    @ManyToOne
    @JoinColumn(name = "team_id")
            @JsonIgnore
    Team adminTeam;
}
