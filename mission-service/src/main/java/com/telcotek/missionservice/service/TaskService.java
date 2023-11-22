package com.telcotek.missionservice.service;

import com.telcotek.missionservice.model.Mission;
import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.MissionRepository;
import com.telcotek.missionservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public List<Task> retrieveAllTasksByMissionId(Long missionId) {
        return taskRepository.findAllByMissionId(missionId);
    }

    public void initializeTask(Task t) {
        LocalDateTime time = LocalDateTime.now();
        t.setProgress(0.0);
        t.setStartTime(time);
        t.setEndedTime(null);
        t.setStatus(Status.TODO);
        taskRepository.save(t);
    }

    public void startTask(Task t) {
        t.setStatus(Status.DOING);
        taskRepository.save(t);
    }

    public void approveTask(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        task.setProgress(100.0);
        task.setStatus(Status.DONE);
        taskRepository.save(task);
    }

    public void changeTasksStatus(Long missionId) {
        Mission mission = missionRepository.getReferenceById(missionId);
        List<Task> tasks = mission.getTasks();
        for(Task task:tasks) {
            task.setStatus(Status.DOING);
            taskRepository.save(task);
        }
    }

    public void createTasks(Long missionId) {
        Mission mission = missionRepository.getReferenceById(missionId);
        for(int i = 0; i < 17; i++) {
            Task task = Task.builder()
                    .name(generateRandomString(8))
                    .mission(mission)
                    .build();
            task.setProgress(0.0);
            task.setStartTime(generateRandomDateTime());
            task.setEndedTime(generateRandomDateTimeAfter(task.getStartTime()));
            task.setStatus(Status.TODO);
            taskRepository.save(task);
            mission.addTask(task);
            missionRepository.save(mission);
            messagingTemplate.convertAndSend("/task-management/tasks", taskRepository.findAll());
        }
    }

    public void addTasksToMission(List<Task> tasks, Long missionId) {
        Mission mission = missionRepository.getReferenceById(missionId);
        for (Task task:tasks){
            Task t =Task.builder()
                    .mission(mission)
                    .name(task.getName())
                    .description(task.getDescription())
                    .build();
            t.setStatus(Status.TODO);
            t.setProgress(0.0);
            t.setStartTime(task.getStartTime());
            t.setEndedTime(task.getEndedTime());
            taskRepository.save(t);
            mission.addTask(task);
            missionRepository.save(mission);
        }
    }
     /** test functions **/
    // task name generator
    public String generateRandomString(int length) {
        // Using characters from 'a' to 'z' and '0' to '9'
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public LocalDateTime generateRandomDateTime() {
        long minDay = LocalDateTime.of(2023, 11, 17, 17, 0).toEpochSecond(ZoneOffset.UTC);
        long maxDay = LocalDateTime.of(2023, 12, 31, 23, 59).toEpochSecond(ZoneOffset.UTC);

        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
    }

    public LocalDateTime generateRandomDateTimeAfter(LocalDateTime dateTime) {
        long minSecond = dateTime.toEpochSecond(ZoneOffset.UTC);
        long maxSecond = LocalDateTime.of(2023, 12, 31, 23, 59).toEpochSecond(ZoneOffset.UTC);

        long randomSecond = ThreadLocalRandom.current().nextLong(minSecond, maxSecond);

        return LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.UTC);
    }

}
