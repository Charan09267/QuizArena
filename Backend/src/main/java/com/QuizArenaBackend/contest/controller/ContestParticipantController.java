package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.dto.ParticipantResponse;
import com.QuizArenaBackend.contest.service.interfaces.ContestParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contests")
@RequiredArgsConstructor
public class ContestParticipantController {

    private final ContestParticipantService participantService;

    @PostMapping("/{contestId}/join")
    public ResponseEntity<ParticipantResponse> joinContest(
            @PathVariable Long contestId
    ) {

        return ResponseEntity.ok(
                participantService.joinContest(contestId)
        );
    }

    @GetMapping("/{contestId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(
            @PathVariable Long contestId
    ) {

        return ResponseEntity.ok(
                participantService.getParticipants(contestId)
        );
    }

}
