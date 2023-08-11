package com.telcotek.missionservice.model;

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
@Table(name = "t_missions")
public class Mission extends WorkFlowMetric{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String title;
    private Integer priorityLevel;
    private Boolean approved;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    List<Task> tasks;

    /** factory pattern */
    public void addTask(Task task) {
        this.tasks.add(task);
    }
}
