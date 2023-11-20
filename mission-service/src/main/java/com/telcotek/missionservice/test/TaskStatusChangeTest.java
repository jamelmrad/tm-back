package com.telcotek.missionservice.test;

import com.telcotek.missionservice.model.Status;
import com.telcotek.missionservice.model.Task;
import com.telcotek.missionservice.repository.TaskRepository;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskStatusChangeTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /** REAL-TIME UPDATES */
    public void notifyTasksListUpdate() {
        messagingTemplate.convertAndSend("/task-management/tasks", taskRepository.findAll());
    }

    @Test
    @RepeatedTest(value = 5, name = "Repeat {currentRepetition} of {totalRepetitions}")
    public void testChangeStatusToDoing() throws InterruptedException {
        List<Task> tasks = taskRepository.findAll();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule status changes for each task every 5 seconds
        scheduler.scheduleAtFixedRate(() -> {
            int randomIndex = ThreadLocalRandom.current().nextInt(tasks.size());
            Task task = tasks.get(randomIndex);
                task.setStatus(Status.DOING);
                taskRepository.save(task);
                messagingTemplate.convertAndSend("/task-management/tasks", taskRepository.findAll());

        }, 0, 5, TimeUnit.SECONDS);

        // Run the test for 25 seconds (5 seconds * 5 repetitions)
        Thread.sleep(25000);

        // Shutdown the scheduler
        scheduler.shutdown();
    }

    private void printTaskStatus(List<Task> tasks) {
        System.out.println("Task statuses at " + LocalDateTime.now() + ":");
        tasks.forEach(task -> System.out.println(task.getName() + ": " + task.getStatus()));
        System.out.println();
    }
}
