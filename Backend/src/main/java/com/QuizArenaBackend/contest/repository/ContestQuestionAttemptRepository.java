package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.ContestQuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestQuestionAttemptRepository
        extends JpaRepository<ContestQuestionAttempt, Long> {

    Optional<ContestQuestionAttempt>
    findByContestAttemptIdAndQuestionId(
            Long attemptId,
            Long questionId
    );

    List<ContestQuestionAttempt>
    findByContestAttemptId(
            Long attemptId
    );

    boolean existsByContestAttemptIdAndQuestionId(
            Long attemptId,
            Long questionId
    );
}