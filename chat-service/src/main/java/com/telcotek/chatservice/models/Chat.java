package com.telcotek.chatservice.models;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "chats")
public class Chat {
    @Id
    private UUID id = UUID.randomUUID();
    private Long mission_id;
    private String mission_title;
    private Integer online_members;

    private List<Message> messages;

}
