package com.telcotek.missionservice.model;

import jakarta.persistence.*;
import lombok.Data;

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

}
