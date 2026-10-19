package com.exercise.backend.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exercise.backend.dto.ChatMessage;

@Service
public class ChatService {
    private final Map<String, List<ChatMessage>> sessions = new LinkedHashMap<>();

    public synchronized String createSession() {
        String id = UUID.randomUUID().toString();
        sessions.put(id, new ArrayList<>());
        return id;
    }

    public synchronized List<String> getSessions() {
        return new ArrayList<>(sessions.keySet());
    }

    public synchronized List<ChatMessage> getHistory(String sessionId) {
        return new ArrayList<>(sessions.getOrDefault(sessionId, List.of()));
    }

    public synchronized String reply(String sessionId, String message) {
        sessions.computeIfAbsent(sessionId, key -> new ArrayList<>());
        sessions.get(sessionId).add(new ChatMessage("user", message));
        String response = buildReply(message);
        sessions.get(sessionId).add(new ChatMessage("assistant", response));
        return response;
    }

    private String buildReply(String message) {
        String lower = message == null ? "" : message.toLowerCase();
        if (lower.contains("diet") || lower.contains("calorie")) {
            return "Keep protein high, aim for steady calories, and check your diet page to compare what you logged against your target.";
        }
        if (lower.contains("exercise") || lower.contains("workout")) {
            return "A balanced week usually mixes cardio and strength work. The exercise page can help you log minutes and see calories burned.";
        }
        if (lower.contains("supplement")) {
            return "Supplements work best as support, not a replacement for food, training, or sleep. Use the supplements page to track what you take.";
        }
        return "I can help with workouts, calories, recovery, and healthy routines. Ask about your plan, meals, exercises, or supplements.";
    }
}
