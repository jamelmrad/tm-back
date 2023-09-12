package com.telcotek.userservice.dto;

import com.telcotek.userservice.model.*;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    String name;
    List<Officer> officers;
    List<Admin> admins;
    Long missionId;
}
