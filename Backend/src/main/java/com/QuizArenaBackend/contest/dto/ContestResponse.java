package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.ContestStatus;
import com.QuizArenaBackend.contest.entity.enums.ContestType;
import com.QuizArenaBackend.contest.entity.enums.ContestVisibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ContestResponse {

    private Long id;

    private String title;

    private String description;

    private ContestVisibility visibility;

    private ContestType contestType;

    private ContestStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationSeconds;

    private Integer maxParticipants;

    private LocalDateTime createdAt;

}
