package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.ContestParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestParticipantRepository
        extends JpaRepository<ContestParticipant, Long> {

    boolean existsByContestIdAndUserId(
            Long contestId,
            Long userId
    );

    Optional<ContestParticipant> findByContestIdAndUserId(
            Long contestId,
            Long userId
    );

    List<ContestParticipant> findByContestId(
            Long contestId
    );

    long countByContestId(
            Long contestId
    );
}