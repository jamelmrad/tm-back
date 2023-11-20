package com.telcotek.chatservice.repository;

import com.telcotek.chatservice.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    Optional<Chat> findByMissionId(Long id);
    List<Chat> findByMissionIdIn(List<Long> missionIds);
}
