package com.telcotek.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("officer")
public class Officer extends User{

    @ManyToOne
    @JoinColumn(name = "team_id")
            @JsonIgnore
    Team team;
}
