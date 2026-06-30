package com.ankurverma.panicbutton.controller;

import com.ankurverma.panicbutton.entity.CoreTask;
import com.ankurverma.panicbutton.entity.MicroSprint;
import com.ankurverma.panicbutton.repository.CoreTaskRepository;
import com.ankurverma.panicbutton.service.TaskBreakdownService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173") // Allows your React app (running on a different port) to make requests without CORS errors
public class TaskController {

    private final TaskBreakdownService taskBreakdownService;
    private final CoreTaskRepository coreTaskRepository;

    public TaskController(TaskBreakdownService taskBreakdownService, CoreTaskRepository coreTaskRepository) {
        this.taskBreakdownService = taskBreakdownService;
        this.coreTaskRepository = coreTaskRepository;
    }

    @PostMapping("/breakdown")
    public ResponseEntity<List<MicroSprint>> triggerPanicButton(@RequestBody CoreTask taskRequest) {

        taskRequest.setStatus("PENDING");
        CoreTask savedTask = coreTaskRepository.save(taskRequest);
        
        List<MicroSprint> generatedSprints = taskBreakdownService.generateMicroSprints(savedTask);

        return ResponseEntity.ok(generatedSprints);
    }
}