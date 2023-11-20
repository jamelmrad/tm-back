package com.telcotek.missionservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class WorkFlowMetric {

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double progress;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endedTime;

    public double calculateDurationInHours() {
        Duration duration = Duration.between(startTime, endedTime);
        return duration.toHours();
    }

    public double calculateDurationInMinutes() {
        Duration duration = Duration.between(startTime, endedTime);
        return duration.toMinutes();
    }
}
