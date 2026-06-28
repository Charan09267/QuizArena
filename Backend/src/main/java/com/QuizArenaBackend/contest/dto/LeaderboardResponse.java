package com.QuizArenaBackend.contest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardResponse {

    private Integer rank;

    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private BigDecimal score;

    private Integer correctAnswers;

    private Integer wrongAnswers;

    private Integer unansweredQuestions;

    private Integer timeTakenSeconds;

    private LocalDateTime submittedAt;
}
