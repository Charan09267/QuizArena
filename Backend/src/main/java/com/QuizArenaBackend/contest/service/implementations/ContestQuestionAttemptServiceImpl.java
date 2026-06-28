package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.common.Utility.AuthenticationFacade;
import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.QuestionAttemptResponse;
import com.QuizArenaBackend.contest.dto.SaveAnswerRequest;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.contest.entity.ContestOption;
import com.QuizArenaBackend.contest.entity.ContestQuestion;
import com.QuizArenaBackend.contest.entity.ContestQuestionAttempt;
import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import com.QuizArenaBackend.contest.repository.ContestAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestOptionRepository;
import com.QuizArenaBackend.contest.repository.ContestQuestionAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestQuestionRepository;
import com.QuizArenaBackend.contest.service.Mappers.QuestionAttemptMapper;
import com.QuizArenaBackend.contest.service.interfaces.ContestEvaluationService;
import com.QuizArenaBackend.contest.service.interfaces.ContestQuestionAttemptService;
import com.QuizArenaBackend.user.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestQuestionAttemptServiceImpl
        implements ContestQuestionAttemptService {

    private final ContestAttemptRepository attemptRepository;
    private final ContestQuestionRepository questionRepository;
    private final ContestOptionRepository optionRepository;
    private final ContestQuestionAttemptRepository questionAttemptRepository;
    private final QuestionAttemptMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    @Transactional
    public QuestionAttemptResponse saveAnswer(
            Long attemptId,
            Long questionId,
            SaveAnswerRequest request) {

        ContestAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attempt not found"));

        UserEntity currentUser = authenticationFacade.getCurrentUser();

        if (!attempt.getParticipant()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalStateException(
                    "Access denied");
        }

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Attempt is no longer active");
        }

        ContestQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found"));

        if (!question.getContest().getId()
                .equals(attempt.getContest().getId())) {

            throw new IllegalStateException(
                    "Question does not belong to this contest");
        }

        ContestOption option = optionRepository.findById(
                        request.getSelectedOptionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Option not found"));

        if (!option.getQuestion().getId()
                .equals(questionId)) {

            throw new IllegalStateException(
                    "Option does not belong to this question");
        }

        ContestQuestionAttempt questionAttempt =
                questionAttemptRepository
                        .findByContestAttemptIdAndQuestionId(
                                attemptId,
                                questionId)
                        .orElse(
                                ContestQuestionAttempt.builder()
                                        .contestAttempt(attempt)
                                        .question(question)
                                        .marksAwarded(BigDecimal.ZERO)
                                        .build()
                        );

        questionAttempt.setSelectedOption(option);
        questionAttempt.setAnsweredAt(LocalDateTime.now());

        ContestQuestionAttempt saved =
                questionAttemptRepository.save(questionAttempt);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionAttemptResponse> getAnswers(
            Long attemptId) {

        ContestAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attempt not found"));

        UserEntity currentUser = authenticationFacade.getCurrentUser();

        if (!attempt.getParticipant()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalStateException(
                    "Access denied");
        }

        return questionAttemptRepository
                .findByContestAttemptId(attemptId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
