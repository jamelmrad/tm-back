package com.telcotek.missionservice.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MissionRequest {
    private String title;
    private Integer priorityLevel;
    private LocalDateTime startTime;
    private LocalDateTime endedTime;
}
