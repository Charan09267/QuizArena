package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.dto.QuestionAttemptResponse;
import com.QuizArenaBackend.contest.dto.SaveAnswerRequest;
import com.QuizArenaBackend.contest.service.interfaces.ContestQuestionAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attempts")
@RequiredArgsConstructor
public class ContestQuestionAttemptController {

    private final ContestQuestionAttemptService service;

    @PostMapping("/{attemptId}/questions/{questionId}/answer")
    public ResponseEntity<QuestionAttemptResponse> saveAnswer(
            @PathVariable Long attemptId,
            @PathVariable Long questionId,
            @RequestBody SaveAnswerRequest request) {

        return ResponseEntity.ok(
                service.saveAnswer(
                        attemptId,
                        questionId,
                        request));
    }

    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<QuestionAttemptResponse>> getAnswers(
            @PathVariable Long attemptId) {

        return ResponseEntity.ok(
                service.getAnswers(attemptId));
    }


}
