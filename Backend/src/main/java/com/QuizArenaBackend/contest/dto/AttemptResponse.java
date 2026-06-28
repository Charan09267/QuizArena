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
public class AttemptResponse {

    private Long id;

    private Long contestId;

    private Long participantId;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    private AttemptStatus status;

    private BigDecimal score;

    private Integer timeTakenSeconds;


    private Integer totalQuestions;

    private Integer correctAnswers;

    private Integer wrongAnswers;

    private Integer unansweredQuestions;



}
