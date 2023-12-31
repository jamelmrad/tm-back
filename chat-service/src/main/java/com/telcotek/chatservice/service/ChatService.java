package com.telcotek.chatservice.service;

import com.telcotek.chatservice.models.*;
import com.telcotek.chatservice.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void create(Long mission_id, String mission_title) {
        Chat chat = Chat.builder()
                .missionId(mission_id)
                .mission_title(mission_title)
                .online_members(0)
                .messages(new ArrayList<>())
                .build();
        chatRepository.save(chat);
        messagingTemplate.convertAndSend("/task-management/chats", chatRepository.findAll()); // no user id ???!
    }

    public Chat getChat(String chatId) {
        return chatRepository.findById(chatId).get();
    }

    public List<Message> getMessages(String chatId) {
        return getChat(chatId).getMessages();
    }

    public void setMessages(Chat chat, List<Message> messages) {
        chat.setMessages(messages);
        chatRepository.save(chat);
    }

    public Message send(
            String chatId,
            String content,
            String teamName,
            String fullName
    ) {
        Chat chat = chatRepository.findById(chatId).get();
        Message message = Message.builder()
                .content(content)
                .sent_time(LocalDateTime.now())
                .sender_fullName(fullName)
                .team_name(teamName)
                .build();
        List<Message> messages = chat.getMessages();
        messages.add(message);
        chat.setMessages(messages);
        messageRepository.save(message);
        chatRepository.save(chat);
        messagingTemplate.convertAndSend("/task-management/chats/"+ chatId, getMessages(chatId));
        return message;
    }

    public void deleteAllMessages(String chatId) {
        Chat chat = chatRepository.findById(chatId).get();

        chat.getMessages().removeAll(chat.getMessages());
        chatRepository.save(chat);
        messagingTemplate.convertAndSend("/task-management/chats/"+ chatId, getMessages(chatId));

        messagingTemplate.convertAndSend("/task-management/chats", chatRepository.findAll());
    }

    public List<Chat> all() {
        return chatRepository.findAll();
    }

    public String getSenderFullName() {
            String userServiceUrl = "http://localhost:8080/api/auth";

                // authentication.getName(); // This gets the email of the authenticated user

                // Build the URL with request parameters
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl);

                // Make the GET request to the authentication service
                return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    public void delete (String chatId) {
        chatRepository.deleteById(chatId);
    }

    public List<Chat> retrieveAllFromMissionIds(String email) {

        /** Get mission Ids from user-service */
        String apiUrl = "http://localhost:8084/api/users/missions-ids";

        // Define the request parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create a request body with the parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);

        // Create an HttpEntity with the request body and headers
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Perform the GET request using RestTemplate
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Long>>() {}
        );

        /** Retrieve chats */
        List<Chat> chats = new ArrayList<>();
        for (Long id:response.getBody()) {
            chats.add(chatRepository.findByMissionId(id).get());
        }
        return chats;
    }

    public List<Chat> getAll(Long userId) {
        List<Chat> chats = new ArrayList<>();

        String url = "http://localhost:8084/api/users/" +userId+"/mission-ids";

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {}
        );

        for (Long id:response.getBody()) {
            chats.add(chatRepository.findByMissionId(id).get());
        }
        return chats;

    }

    public List<Chat> getAllUserChats(String missionIds) {
        List<Long> ids = Arrays.stream(missionIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        return chatRepository.findByMissionIdIn(ids);
    }

    public List<Chat> getAllMod() {
        return  chatRepository.findAll();
    }

}
