package com.telcotek.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    private String name;
    private Long missionId;
    private Integer numberOfTeamMembers;

    @OneToMany(mappedBy = "team")
    List<Officer> officers;

    @OneToOne(mappedBy = "team",cascade = CascadeType.MERGE)
    SuperAdmin superAdmin;

    @OneToMany(mappedBy = "team")
    List<Admin> admins;

    public SuperAdmin getSuperAdmin() {
        return this.superAdmin;
    }
}
