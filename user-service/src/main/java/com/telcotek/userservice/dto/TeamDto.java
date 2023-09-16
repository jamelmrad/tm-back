package com.telcotek.userservice.dto;

import com.telcotek.userservice.model.*;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    String name;
    List<User> officers;
    List<User> admins;
    User superAdmin;
    Long missionId;
}
