package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.ContestType;
import com.QuizArenaBackend.contest.entity.enums.ContestVisibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateContestRequest {

    private String title;

    private String description;

    private ContestVisibility visibility;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationSeconds;

    private Integer maxParticipants;

    private ContestType contestType;

}
