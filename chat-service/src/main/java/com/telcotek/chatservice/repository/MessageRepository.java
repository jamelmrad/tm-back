package com.telcotek.chatservice.repository;

import com.telcotek.chatservice.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
