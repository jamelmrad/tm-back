package com.telcotek.missionservice.dto;

import com.telcotek.missionservice.model.Task;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MissionRequest {
    private String title;
    private Integer priorityLevel;
    private LocalDateTime startTime;
    private LocalDateTime endedTime;
    private Double teams;

    List<Task> tasks = new ArrayList<>();
}
