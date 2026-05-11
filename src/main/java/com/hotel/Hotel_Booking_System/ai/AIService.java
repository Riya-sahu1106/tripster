package com.hotel.Hotel_Booking_System.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class AIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String apiKey;

    private final String URL = "https://api.groq.com/openai/v1/chat/completions";

    public String getResponse(String userMessage, String hotelData) {
        try {
            String systemPrompt = "You are a helpful hotel booking assistant for Tripster. " +
                "Answer questions about hotels, bookings, weather, travel tips, anything. " +
                "Be friendly and concise. Use emojis. Available hotels:\n" + hotelData;

            // System message
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);

            // User message
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            // Body
            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.3-70b-versatile"); // free model
            body.put("messages", List.of(systemMsg, userMsg));
            body.put("max_tokens", 512);
            body.put("temperature", 0.7);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);

            Map res = response.getBody();
            List choices = (List) res.get("choices");
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");
            return message.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I'm having trouble right now. Please try again! 😊";
        }
    }
}