package com.telcotek.chatservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String content;
    private String sender_fullName; // get it from auth service : done
    private String team_name; // get it from user-service
    private LocalDateTime sent_time;
    private String chatId;

    //private List<Object> viewers; may be added later (PROBLEM)
}
