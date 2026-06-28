package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.AttemptResponse;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import org.springframework.stereotype.Component;

@Component
public class AttemptMapper {

    public AttemptResponse toResponse(
            ContestAttempt attempt
    ) {

        return AttemptResponse.builder()
                .id(attempt.getId())
                .contestId(attempt.getContest().getId())
                .participantId(attempt.getParticipant().getId())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .status(attempt.getStatus())
                .score(attempt.getScore())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .wrongAnswers(attempt.getWrongAnswers())
                .unansweredQuestions(attempt.getUnansweredQuestions())
                .timeTakenSeconds(attempt.getTimeTakenSeconds())
                .build();
    }

}