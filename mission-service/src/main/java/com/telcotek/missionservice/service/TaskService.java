package com.telcotek.missionservice.service;

import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

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

}
