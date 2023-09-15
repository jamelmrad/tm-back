package com.telcotek.chatservice.repository;

import com.telcotek.chatservice.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    Optional<Chat> findByMission_id(Long s);
}
