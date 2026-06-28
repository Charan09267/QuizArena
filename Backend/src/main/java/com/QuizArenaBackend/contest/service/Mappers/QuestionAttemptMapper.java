package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.QuestionAttemptResponse;
import com.QuizArenaBackend.contest.entity.ContestQuestionAttempt;
import org.springframework.stereotype.Component;

@Component
public class QuestionAttemptMapper {

    public QuestionAttemptResponse toResponse(
            ContestQuestionAttempt attempt
    ) {

        return QuestionAttemptResponse.builder()
                .id(attempt.getId())
                .questionId(attempt.getQuestion().getId())
                .selectedOptionId(
                        attempt.getSelectedOption() != null
                                ? attempt.getSelectedOption().getId()
                                : null
                )
                .isCorrect(attempt.getIsCorrect())
                .marksAwarded(attempt.getMarksAwarded())
                .answeredAt(attempt.getAnsweredAt())
                .build();
    }
}
