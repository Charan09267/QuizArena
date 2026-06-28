package com.QuizArenaBackend.contest.service.interfaces;

import com.QuizArenaBackend.contest.dto.ParticipantResponse;

import java.util.List;

public interface ContestParticipantService {
    ParticipantResponse joinContest(Long contestId);

    List<ParticipantResponse> getParticipants(
            Long contestId
    );
}
