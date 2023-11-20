package com.telcotek.missionservice.service;

import com.telcotek.missionservice.model.Metrics;
import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.MissionRepository;
import com.telcotek.missionservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricsService {
    @Autowired
    MissionRepository missionRepository;

    @Autowired
    TaskRepository taskRepository;

    public Integer totalMissions() {
        return missionRepository.findAll().size();
    }

    public Integer missionsSinceLastVisit(LocalDateTime lastVisit) {
        return null; // needs created_at attribute in mission model
    }

    public Integer totalTasks() {
        return taskRepository.findAll().size();
    }

    public Integer taskSinceLastVisit(LocalDateTime lastVisit) {
        return null;
    }

    public Double totalProgress(List<Mission> missions) {
        Double progressSum = 0.0;
        if (missions.isEmpty()) {
            return 0.0;
        } else {
            for(Mission mission:missions) {
                progressSum = progressSum + mission.getProgress();
            }
            return progressSum / missions.size();
        }
    }

    public Double missionProgress(Mission mission) {
        Double progressSum = 0.0;
        if (mission.getTasks().isEmpty()) {
            return 0.0;
        } else {
            for(Task task:mission.getTasks()) {
                progressSum = progressSum + task.getProgress();
            }
            return progressSum / mission.getTasks().size();
        }
    }

    public Double doneTasksPerHour(Mission mission) {
        List<Task> tasks = mission.getTasks();

        List<Task> doneTasks = tasks.stream()
                .filter(task -> task.getStatus().equals(Status.DONE))
                .collect(Collectors.toList());

        double totalHoursDone = tasks.stream()
                .filter(task -> task.getStatus().equals(Status.DONE))
                .mapToDouble(Task::calculateDurationInHours)
                .sum();

        double tasksPerHour = doneTasks.size() / totalHoursDone;

        return tasksPerHour;
    }

    public Double doneTasksPerMinute(Mission mission) {

        List<Task> doneTasks = mission.getTasks().stream()
                .filter(task -> task.getStatus().equals(Status.DONE))
                .collect(Collectors.toList());

        double totalMinutesDone = mission.getTasks().stream()
                .filter(task -> task.getStatus().equals(Status.DONE))
                .mapToDouble(Task::calculateDurationInMinutes)
                .sum();

        double tasksPerMinutes = doneTasks.size() / totalMinutesDone;

        return tasksPerMinutes;
    }

    /** use this for api === > Observe data changes in realtime with automated tests **/
    public Metrics getMissionMetrics(Mission mission) {
        Double res1 = missionProgress(mission);
        Double res2 = doneTasksPerHour(mission);
        Double res3 = doneTasksPerMinute(mission);
        return new Metrics(res1,res2,res3);
    }

    /** Sequential Workflows: */
    public double calculateCycleTime(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            Duration duration = Duration.between(mission.getStartTime(), mission.getEndedTime());
            return duration.toMinutes(); // Convert duration to desired time unit
        }
        return -1; // Indicate error or no mission found
    }

    public double calculateThroughput(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            return mission.getTasks().size() / calculateCycleTime(missionId); // Tasks per time unit
        }
        return -1; // Indicate error or no mission found
    }

    /** Parallel Workflows: */
    public double calculateEfficiency(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            double totalTaskDuration = 0;
            for (Task task : mission.getTasks()) {
                Duration taskDuration = Duration.between(task.getStartTime(), task.getEndedTime());
                totalTaskDuration += taskDuration.toMinutes(); // Accumulate task durations
            }

            Duration missionDuration = Duration.between(mission.getStartTime(), mission.getEndedTime());
            double maxPossibleDuration = missionDuration.toMinutes(); // Mission duration in minutes
            return (totalTaskDuration / maxPossibleDuration) * 100; // Efficiency percentage
        }
        return -1; // Indicate error or no mission found
    }

    public double calculateConcurrencyRate(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            // Implement logic to determine overlapping task durations
            // This might involve checking intersections of task time intervals within the mission
            // and calculating the percentage of time where multiple tasks overlap.
            // You'd likely need to iterate through tasks, compare their start and end times, and identify overlaps.

            // For example:
            // double parallelTime = ...; // Calculate total parallel time
            // Duration missionDuration = Duration.between(mission.getStartTime(), mission.getEndTime());
            // return (parallelTime / missionDuration.toMinutes()) * 100; // Concurrency rate percentage
        }
        return -1; // Indicate error or no mission found
    }

    /** State machine Workflows: */
    public double calculateStateTransitionRate(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            // Assuming a mission has a state attribute representing its current state
            List<Status> states = new ArrayList<>(); // Store unique states
            int transitions = 0;

            for (Task task : mission.getTasks()) {
                Status currentState = task.getStatus(); // Get the state of the task
                if (!states.contains(currentState)) {
                    states.add(currentState); // Add unique states to the list
                }
            }

            transitions = states.size() - 1; // Transitions between states
            return transitions; // Rate of state transitions
        }
        return -1; // Indicate error or no mission found
    }

    public double calculateAverageTimeInState(Long missionId, Status state) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            int stateOccurrences = 0;
            Duration totalDuration = Duration.ZERO;

            for (Task task : mission.getTasks()) {
                if (task.getStatus().equals(state)) {
                    Duration taskDuration = Duration.between(task.getStartTime(), task.getEndedTime());
                    totalDuration = totalDuration.plus(taskDuration);
                    stateOccurrences++;
                }
            }

            if (stateOccurrences > 0) {
                return totalDuration.toMinutes() / stateOccurrences; // Average time in state
            }
        }
        return -1; // Indicate error or no mission found
    }

    /** Rule-based Workflows: */ // maybe used in case if reporting is necessary
    public double calculateComplianceRate(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            int compliantTasks = 0;

            for (Task task : mission.getTasks()) {
                if (isTaskCompliant(task)) {
                    compliantTasks++;
                }
            }

            return (double) compliantTasks / mission.getTasks().size() * 100; // Compliance rate
        }
        return -1; // Indicate error or no mission found
    }

    public double calculateRuleViolationRate(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            int violationCount = 0;

            for (Task task : mission.getTasks()) {
                if (!isTaskCompliant(task)) {
                    violationCount++;
                }
            }

            return (double) violationCount / mission.getTasks().size() * 100; // Violation rate
        }
        return -1; // Indicate error or no mission found
    }

    private boolean isTaskCompliant(Task task) {
        // Implement logic to determine if a task complies with predefined rules
        // Return true if compliant, false otherwise
        // Example: Check task attributes against predefined rules
        // Example: if(task.getAttribute() > threshold) { return true; } else { return false; }
        return true; // Placeholder - replace with actual compliance check logic
    }

    /** Flexibility ratio */
    public double calculateFlexibilityRatio(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElse(null);
        if (mission != null) {
            int changedTasks = 0;

            for (Task task : mission.getTasks()) {
                if (isTaskChanged(task)) {
                    changedTasks++;
                }
            }

            return (double) changedTasks / mission.getTasks().size() * 100; // Flexibility ratio
        }
        return -1; // Indicate error or no mission found
    }

    private boolean isTaskChanged(Task task) {
        // Implement logic to determine if a task has been changed or modified
        // This could involve comparing task attributes or properties against previous versions or criteria.
        // Return true if changed, false otherwise.
        // Example: Compare task attributes against stored historical values to detect changes.
        // Example: if(task.getCurrentValue() != task.getPreviousValue()) { return true; } else { return false; }
        return true; // Placeholder - replace with actual change detection logic
    }

}
