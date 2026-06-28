package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.ContestResultResponse;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import org.springframework.stereotype.Component;

@Component
public class ContestResultMapper {

    public ContestResultResponse toResponse(
            ContestAttempt attempt) {

        return ContestResultResponse.builder()
                .contestId(attempt.getContest().getId())
                .attemptId(attempt.getId())
                .score(attempt.getScore())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .wrongAnswers(attempt.getWrongAnswers())
                .unansweredQuestions(
                        attempt.getUnansweredQuestions())
                .timeTakenSeconds(
                        attempt.getTimeTakenSeconds())
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }

}
