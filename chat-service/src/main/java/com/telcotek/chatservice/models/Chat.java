package com.telcotek.chatservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private Long missionId;
    private String mission_title;
    private Integer online_members;

    private List<Message> messages;

}
