package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.dto.CreateContestRequest;
import com.QuizArenaBackend.contest.dto.UpdateContestRequest;
import com.QuizArenaBackend.contest.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @PostMapping
    public ResponseEntity<ContestResponse> createContest(
            @RequestBody CreateContestRequest request) {

        return ResponseEntity.ok(
                contestService.createContest(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestResponse> getContest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                contestService.getContestById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ContestResponse>> getAllContests() {

        return ResponseEntity.ok(
                contestService.getAllContests()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContestResponse> updateContest(
            @PathVariable Long id,
            @RequestBody UpdateContestRequest request) {

        return ResponseEntity.ok(
                contestService.updateContest(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(
            @PathVariable Long id) {

        contestService.deleteContest(id);

        return ResponseEntity.noContent().build();
    }
}
