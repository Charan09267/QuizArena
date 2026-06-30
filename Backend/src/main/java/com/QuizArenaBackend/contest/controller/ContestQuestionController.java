package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.dto.CreateQuestionRequest;
import com.QuizArenaBackend.contest.dto.QuestionResponse;
import com.QuizArenaBackend.contest.service.interfaces.ContestQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contests")
@RequiredArgsConstructor
public class ContestQuestionController {

    private final ContestQuestionService questionService;

    @PostMapping("/{contestId}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long contestId,
            @Valid  @RequestBody CreateQuestionRequest request) {

        return ResponseEntity.ok(
                questionService.addQuestion(contestId, request));
    }

    @GetMapping("/{contestId}/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions(
            @PathVariable Long contestId) {

        return ResponseEntity.ok(
                questionService.getQuestions(contestId));
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody CreateQuestionRequest request) {

        return ResponseEntity.ok(
                questionService.updateQuestion(questionId, request));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId) {

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }
}
