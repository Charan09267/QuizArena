package com.QuizArenaBackend.contest.service.interfaces;

import com.QuizArenaBackend.contest.entity.ContestAttempt;

public interface ContestEvaluationService {

    void evaluateAttempt(ContestAttempt attempt);

}
