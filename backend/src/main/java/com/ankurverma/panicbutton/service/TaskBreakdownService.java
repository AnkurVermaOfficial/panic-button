package com.ankurverma.panicbutton.service;

import com.ankurverma.panicbutton.entity.CoreTask;
import com.ankurverma.panicbutton.entity.MicroSprint;
import com.ankurverma.panicbutton.repository.MicroSprintRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskBreakdownService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
   //private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
   // private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // For parsing JSON

    private final MicroSprintRepository microSprintRepository;

    // Injecting the repository via constructor
    public TaskBreakdownService(MicroSprintRepository microSprintRepository) {
        this.microSprintRepository = microSprintRepository;
    }

    public List<MicroSprint> generateMicroSprints(CoreTask task) {
        String systemPrompt = """
            You are an autonomous productivity agent. Your job is to take a complex, 
            overwhelming user task and break it down into consecutive, actionable micro-sprints.
            1. Keep sprints between 15 to 45 minutes to reduce cognitive load.
            2. Focus on action verbs.
            3. Suggest a tool or URL if applicable.
            Output Format: Return a JSON array matching the required schema.
            Schema: [{ "title": "String", "duration_mins": Integer, "sequence": Integer, "action_url": "String or null" }]
            """;

        String userPrompt = "Break down this task: " + task.getTitle() + " Due by: " + task.getDeadline();

        // 1. Build the API payload
        Map<String, Object> requestBody = new HashMap<>();

        // Add system prompt
        requestBody.put("system_instruction", Map.of("parts", List.of(Map.of("text", systemPrompt))));

        // Add user prompt
        requestBody.put("contents", List.of(Map.of("parts", List.of(Map.of("text", userPrompt)))));

        // CRITICAL: Force Gemini to output raw JSON directly without conversational text
        requestBody.put("generationConfig", Map.of("responseMimeType", "application/json"));

        // 2. Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Package and make the call
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String fullUrl = GEMINI_API_URL + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, request, String.class);

            // 4. Extract the inner text block containing our array out of the Gemini response wrapper
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String rawJsonArrayText = rootNode.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            // 5. Transform the JSON array string into MicroSprint objects
            List<MicroSprint> microSprints = objectMapper.readValue(
                    rawJsonArrayText,
                    new TypeReference<List<MicroSprint>>() {}
            );

            // 6. Connect each sprint to the parent task and save everything to the DB
            for (MicroSprint sprint : microSprints) {
                sprint.setCoreTask(task);
            }

            microSprintRepository.saveAll(microSprints);
            System.out.println("Success! Generated and saved " + microSprints.size() + " micro-sprints to the DB.");

            return microSprints;

        } catch (Exception e) {
            System.err.println("Gemini API overloaded or failed: " + e.getMessage());

            MicroSprint mockSprint1 = new MicroSprint();
            mockSprint1.setTitle("Outline the core requirements and goals");
            mockSprint1.setDurationMins(20);
            mockSprint1.setSequence(1);
            mockSprint1.setCoreTask(task);

            MicroSprint mockSprint2 = new MicroSprint();
            mockSprint2.setTitle("Draft the initial document/code");
            mockSprint2.setDurationMins(45);
            mockSprint2.setSequence(2);
            mockSprint2.setCoreTask(task);

            microSprintRepository.saveAll(List.of(mockSprint1, mockSprint2));

            return List.of(mockSprint1, mockSprint2);
        }
    }
}