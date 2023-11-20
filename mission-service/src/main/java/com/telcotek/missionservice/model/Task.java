package com.telcotek.missionservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_tasks")
public class Task extends WorkFlowMetric{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String name;
    private String description;
    private String contributors; // json of all contributors by id "{1,2,3,5,7}"

    @ManyToOne
            @JsonIgnore
    Mission mission;
}
