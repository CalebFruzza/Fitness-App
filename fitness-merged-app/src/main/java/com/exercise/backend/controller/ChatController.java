package com.exercise.backend.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.backend.dto.ChatMessage;
import com.exercise.backend.dto.ChatRequest;
import com.exercise.backend.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/new", produces = MediaType.TEXT_PLAIN_VALUE)
    public String createChat() {
        return chatService.createSession();
    }

    @GetMapping("/sessions")
    public List<String> getSessions() {
        return chatService.getSessions();
    }

    @GetMapping("/history/{sessionId}")
    public List<ChatMessage> getHistory(@PathVariable String sessionId) {
        return chatService.getHistory(sessionId);
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestBody ChatRequest request) {
        return chatService.reply(request.getSessionId(), request.getMessage());
    }
}
