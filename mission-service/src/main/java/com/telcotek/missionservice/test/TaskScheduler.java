package com.telcotek.missionservice.test;

import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.TaskRepository;
import com.telcotek.missionservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskScheduler {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private int currentIndex = 0;

    // change task every 5 seconds
    /*
    @Scheduled(fixedRate = 5000)
    public void tasks() {
        // Check if there are tasks in the list
        if (!taskRepository.findAll().isEmpty()) {
            // Get the current task
            Task currentTask = taskRepository.findAll().get(currentIndex);

            Status currentStatus = currentTask.getStatus();
            Status newStatus = getNextStatus(currentStatus);
            currentTask.setStatus(newStatus);

            taskRepository.save(currentTask);

            messagingTemplate.convertAndSend("/task-management/tasks", taskRepository.findAll());

            System.out.println("Task " + currentTask.getId() + " status updated to: " + newStatus);

            // Move to the next task
            currentIndex = (currentIndex + 1) % taskRepository.findAll().size();
        }
    }

     */
    private Status getNextStatus(Status currentStatus) {
        switch (currentStatus) {
            case TODO:
                return Status.DOING;
            case DOING:
                return Status.DONE;
            default:
                throw new IllegalStateException("Unexpected value: " + currentStatus);
        }
    }

}

