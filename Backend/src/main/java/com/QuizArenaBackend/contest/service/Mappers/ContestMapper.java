package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.entity.Contest;
import org.springframework.stereotype.Component;

@Component
public class ContestMapper {

    public ContestResponse toResponse(Contest contest) {

        return ContestResponse.builder()
                .id(contest.getId())
                .title(contest.getTitle())
                .description(contest.getDescription())
                .visibility(contest.getVisibility())
                .contestType(contest.getContestType())
                .status(contest.getStatus())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .durationSeconds(contest.getDurationSeconds())
                .maxParticipants(contest.getMaxParticipants())
                .createdAt(contest.getCreatedAt())
                .build();
    }
}
