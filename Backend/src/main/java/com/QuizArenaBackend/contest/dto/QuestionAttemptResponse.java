package com.QuizArenaBackend.contest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAttemptResponse {

    private Long id;

    private Long questionId;

    private Long selectedOptionId;

    private Boolean isCorrect;

    private BigDecimal marksAwarded;

    private LocalDateTime answeredAt;

}