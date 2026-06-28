package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestAttemptRepository
        extends JpaRepository<ContestAttempt, Long> {

    Optional<ContestAttempt> findByParticipantId(
            Long participantId
    );

    boolean existsByParticipantId(
            Long participantId
    );

    @Query("""

            SELECT a
FROM ContestAttempt a
WHERE a.contest.id = :contestId
AND a.status IN :statuses
ORDER BY
a.score DESC,
a.timeTakenSeconds ASC,
a.submittedAt ASC
""")
    List<ContestAttempt> findLeaderboardByContestId(
            @Param("contestId") Long contestId,
            @Param("statuses") List<AttemptStatus> statuses);
}