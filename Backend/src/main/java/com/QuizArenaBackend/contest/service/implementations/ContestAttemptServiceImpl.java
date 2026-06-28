package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.common.Utility.AuthenticationFacade;
import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.AttemptResponse;
import com.QuizArenaBackend.contest.dto.ContestResultResponse;
import com.QuizArenaBackend.contest.entity.Contest;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.contest.entity.ContestParticipant;
import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import com.QuizArenaBackend.contest.entity.enums.ContestStatus;
import com.QuizArenaBackend.contest.entity.enums.ParticipantStatus;
import com.QuizArenaBackend.contest.repository.ContestAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestParticipantRepository;
import com.QuizArenaBackend.contest.repository.ContestRepository;
import com.QuizArenaBackend.contest.service.Mappers.AttemptMapper;
import com.QuizArenaBackend.contest.service.Mappers.ContestResultMapper;
import com.QuizArenaBackend.contest.service.interfaces.ContestAttemptService;
import com.QuizArenaBackend.contest.service.interfaces.ContestEvaluationService;
import com.QuizArenaBackend.user.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class ContestAttemptServiceImpl implements ContestAttemptService {

    private final AuthenticationFacade authenticationFacade;
    private final ContestRepository contestRepository;
    private final ContestParticipantRepository participantRepository;
    private final ContestAttemptRepository attemptRepository;
    private final AttemptMapper attemptMapper;
    private final ContestEvaluationService evaluationService;
    private final ContestResultMapper resultMapper;



    @Override
    @Transactional
    public AttemptResponse startContest(Long contestId) {

        UserEntity currentUser = authenticationFacade.getCurrentUser();

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        if (contest.getStatus() != ContestStatus.LIVE) {
            throw new IllegalStateException(
                    "Contest is not live");
        }

        ContestParticipant participant =
                participantRepository
                        .findByContestIdAndUserId(
                                contestId,
                                currentUser.getId())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "You have not joined this contest"));

        if (attemptRepository.existsByParticipantId(
                participant.getId())) {

            throw new IllegalStateException(
                    "Contest already started");
        }

        ContestAttempt attempt = ContestAttempt.builder()
                .contest(contest)
                .participant(participant)
                .status(AttemptStatus.IN_PROGRESS)
                .score(BigDecimal.ZERO)
                .totalQuestions(contest.getQuestions().size())
                .correctAnswers(0)
                .wrongAnswers(0)
                .unansweredQuestions(contest.getQuestions().size())
                .startedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(contest.getDurationSeconds()))
                .build();

        ContestAttempt saved =
                attemptRepository.save(attempt);

        participant.setStatus(
                ParticipantStatus.STARTED);

        return attemptMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptResponse getAttempt(Long attemptId) {

        ContestAttempt attempt =
                attemptRepository.findById(attemptId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attempt not found"));

        UserEntity currentUser =
                authenticationFacade.getCurrentUser();

        if (!attempt.getParticipant()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalStateException(
                    "You are not allowed to access this attempt");
        }

        return attemptMapper.toResponse(attempt);
    }

    @Override
    @Transactional
    public AttemptResponse submitContest(Long attemptId) {

        ContestAttempt attempt =
                attemptRepository.findById(attemptId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attempt not found"));

        UserEntity currentUser =
                authenticationFacade.getCurrentUser();

        if (!attempt.getParticipant()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalStateException(
                    "Access denied");
        }

        if (attempt.getStatus()
                != AttemptStatus.IN_PROGRESS) {

            throw new IllegalStateException(
                    "Attempt already submitted");
        }

        evaluationService.evaluateAttempt(attempt);

        attempt.setSubmittedAt(LocalDateTime.now());

        attempt.setStatus(AttemptStatus.SUBMITTED);

        attempt.setTimeTakenSeconds(
                (int) Duration.between(
                                attempt.getStartedAt(),
                                attempt.getSubmittedAt())
                        .getSeconds());

        attempt.getParticipant()
                .setStatus(ParticipantStatus.SUBMITTED);

        attemptRepository.save(attempt);

        return attemptMapper.toResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public ContestResultResponse getResult(
            Long attemptId) {

        ContestAttempt attempt =
                attemptRepository.findById(attemptId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attempt not found"));

        UserEntity currentUser =
                authenticationFacade.getCurrentUser();

        if (!attempt.getParticipant()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalStateException(
                    "Access denied");
        }

        if (attempt.getStatus() == AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Contest has not been submitted yet");
        }

        return resultMapper.toResponse(attempt);
    }
}
