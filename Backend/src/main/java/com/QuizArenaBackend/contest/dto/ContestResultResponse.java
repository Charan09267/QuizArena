package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestResultResponse {

    private Long contestId;

    private Long attemptId;

    private BigDecimal score;

    private Integer totalQuestions;

    private Integer correctAnswers;

    private Integer wrongAnswers;

    private Integer unansweredQuestions;

    private Integer timeTakenSeconds;

    private AttemptStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

}
