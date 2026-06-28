package com.QuizArenaBackend.contest.service.interfaces;

import com.QuizArenaBackend.contest.dto.AttemptResponse;
import com.QuizArenaBackend.contest.dto.ContestResultResponse;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import org.springframework.transaction.annotation.Transactional;

public interface ContestAttemptService {

    AttemptResponse startContest(Long contestId);

    AttemptResponse getAttempt(Long attemptId);

    AttemptResponse submitContest(Long attemptId);

    ContestResultResponse getResult(Long attemptId);
}