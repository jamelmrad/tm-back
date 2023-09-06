package com.telcotek.chatservice.repository;

import com.telcotek.chatservice.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
