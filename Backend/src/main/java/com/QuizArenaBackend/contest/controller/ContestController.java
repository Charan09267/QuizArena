package com.QuizArenaBackend.contest.controller;

import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.dto.CreateContestRequest;
import com.QuizArenaBackend.contest.dto.LeaderboardResponse;
import com.QuizArenaBackend.contest.dto.UpdateContestRequest;
import com.QuizArenaBackend.contest.service.interfaces.ContestService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody CreateContestRequest request) {

        return ResponseEntity.ok(
                contestService.createContest(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestResponse> getContest(
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    contestService.getContestById(id)
            );
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
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
            @Valid @RequestBody UpdateContestRequest request) {

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

    @PostMapping("/{contestId}/publish")
    public ResponseEntity<ContestResponse> publishContest(
            @PathVariable Long contestId) {

        return ResponseEntity.ok(
                contestService.publishContest(contestId)
        );
    }


    @PostMapping("/{contestId}/cancel")
    public ResponseEntity<ContestResponse> cancelContest(
            @PathVariable Long contestId) {

        return ResponseEntity.ok(
                contestService.cancelContest(contestId)
        );
    }

    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardResponse>>
    getLeaderboard(
            @PathVariable Long contestId){

        return ResponseEntity.ok(
                contestService.getLeaderboard(
                        contestId));
    }


}
