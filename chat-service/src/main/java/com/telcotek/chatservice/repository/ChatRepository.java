package com.telcotek.chatservice.repository;

import com.telcotek.chatservice.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends MongoRepository<Chat, UUID> {
}
