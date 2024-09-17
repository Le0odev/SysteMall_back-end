package com.example.SysteMall_backend.controller;// src/main/java/com/exemplo/feedback/FeedbackController.java

import com.example.SysteMall_backend.entity.Feedback;
import com.example.SysteMall_backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.0.0.107:5173", "http://10.0.0.108:5173"})
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody Feedback feedback) {
        Feedback entity = new Feedback();
        entity.setRating(feedback.getRating());
        entity.setTimestamp(feedback.getTimestamp());
        feedbackRepository.save(entity);

        return new ResponseEntity<>("Feedback recebido com sucesso!", HttpStatus.OK);
    }
}
