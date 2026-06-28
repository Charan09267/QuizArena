package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.dto.AttemptResponse;
import com.QuizArenaBackend.contest.dto.ContestResultResponse;
import com.QuizArenaBackend.contest.service.interfaces.ContestAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attempts")
@RequiredArgsConstructor
public class ContestAttemptController {

    private final ContestAttemptService contestAttemptService;

    @PostMapping("/{contestId}/start")
    public ResponseEntity<AttemptResponse> startContest(
            @PathVariable Long contestId) {

        return ResponseEntity.ok(
                contestAttemptService.startContest(contestId));
    }

    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<AttemptResponse> submitContest(
            @PathVariable Long attemptId) {

        return ResponseEntity.ok(
                contestAttemptService.submitContest(attemptId));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<AttemptResponse> getAttempt(
            @PathVariable Long attemptId) {

        return ResponseEntity.ok(
                contestAttemptService.getAttempt(attemptId));
    }

    @GetMapping("/{attemptId}/result")
    public ResponseEntity<ContestResultResponse> getResult(
            @PathVariable Long attemptId) {

        return ResponseEntity.ok(
                contestAttemptService.getResult(attemptId));
    }
}