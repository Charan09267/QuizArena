package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.ParticipantStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {

    private Long id;

    private Long userId;

    private String username;

    private LocalDateTime joinedAt;

    private ParticipantStatus status;

}
