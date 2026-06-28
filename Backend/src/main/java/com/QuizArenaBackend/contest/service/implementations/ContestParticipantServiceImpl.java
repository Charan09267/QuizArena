package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.common.Utility.AuthenticationFacade;
import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.ParticipantResponse;
import com.QuizArenaBackend.contest.entity.Contest;
import com.QuizArenaBackend.contest.entity.ContestParticipant;
import com.QuizArenaBackend.contest.entity.enums.ContestStatus;
import com.QuizArenaBackend.contest.entity.enums.ParticipantStatus;
import com.QuizArenaBackend.contest.repository.ContestParticipantRepository;
import com.QuizArenaBackend.contest.repository.ContestRepository;
import com.QuizArenaBackend.contest.service.Mappers.ParticipantMapper;
import com.QuizArenaBackend.contest.service.interfaces.ContestParticipantService;
import com.QuizArenaBackend.user.Entity.UserEntity;
import com.QuizArenaBackend.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestParticipantServiceImpl
        implements ContestParticipantService {

    private final ContestRepository contestRepository;
    private final ContestParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ParticipantMapper participantMapper;

    @Override
    public ParticipantResponse joinContest(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        UserEntity currentUser = authenticationFacade.getCurrentUser();

        // Allow joining only before the contest starts
        if (contest.getStatus() != ContestStatus.UPCOMING) {
            throw new IllegalStateException(
                    "Contest is not open for registration");
        }

        // Prevent duplicate registration
        if (participantRepository.existsByContestIdAndUserId(
                contestId,
                currentUser.getId())) {

            throw new IllegalStateException(
                    "You have already joined this contest");
        }

        // Check participant limit
        long participants =
                participantRepository.countByContestId(contestId);

        if (contest.getMaxParticipants() != null &&
                participants >= contest.getMaxParticipants()) {

            throw new IllegalStateException(
                    "Maximum participants reached");
        }

        ContestParticipant participant =
                ContestParticipant.builder()
                        .contest(contest)
                        .user(currentUser)
                        .status(ParticipantStatus.REGISTERED)
                        .build();

        ContestParticipant saved =
                participantRepository.save(participant);

        return participantMapper.toResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(Long contestId) {

        if (!contestRepository.existsById(contestId)) {
            throw new ResourceNotFoundException("Contest not found");
        }

        return participantRepository.findByContestId(contestId)
                .stream()
                .map(participantMapper::toResponse)
                .toList();
    }
}
