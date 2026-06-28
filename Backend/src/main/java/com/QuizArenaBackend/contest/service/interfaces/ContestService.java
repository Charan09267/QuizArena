package com.QuizArenaBackend.contest.service.interfaces;

import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.dto.CreateContestRequest;
import com.QuizArenaBackend.contest.dto.LeaderboardResponse;
import com.QuizArenaBackend.contest.dto.UpdateContestRequest;

import java.util.List;

public interface ContestService {

    ContestResponse createContest(CreateContestRequest request);

    ContestResponse getContestById(Long contestId);

    List<ContestResponse> getAllContests();

    ContestResponse updateContest(
            Long contestId,
            UpdateContestRequest request);

    void deleteContest(Long contestId);

    ContestResponse publishContest(Long contestId);

    ContestResponse cancelContest(Long contestId);

    void activateContests();

    void completeContests();

    List<LeaderboardResponse> getLeaderboard(Long contestId);

}
