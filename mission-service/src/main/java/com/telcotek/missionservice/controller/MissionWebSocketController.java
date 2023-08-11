package com.telcotek.missionservice.controller;

import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MissionWebSocketController {


    @Autowired
    MissionService missionService;

    @MessageMapping("/missions")
    @SendTo("/task-management/missions")
    public List<Mission> getMissionListUpdate() {
        return missionService.retrieveAllMissions();
    }

    @MessageMapping("/missions/{missionId}")
    @SendTo("/task-management/missions/{missionId}")
    public Mission getMissionUpdate(@DestinationVariable Long missionId) {
        return missionService.retrieveMissionById(missionId);
    }

}
