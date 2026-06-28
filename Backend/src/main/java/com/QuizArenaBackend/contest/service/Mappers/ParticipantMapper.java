package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.ParticipantResponse;
import com.QuizArenaBackend.contest.entity.ContestParticipant;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public ParticipantResponse toResponse(
            ContestParticipant participant
    ) {

        return ParticipantResponse.builder()
                .id(participant.getId())
                .userId(participant.getUser().getId())
                .username(participant.getUser().getUsername())
                .joinedAt(participant.getJoinedAt())
                .status(participant.getStatus())
                .build();
    }

}
