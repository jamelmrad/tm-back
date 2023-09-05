package com.telcotek.chatservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "messages")
public class Message {
    @Id
    private Long id;
    private String content;
    private Long sender_fullname;
    private String team_name;
    private LocalDateTime sent_time;

    private List<Object> viewers;
}
