package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.ContestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestQuestionRepository
        extends JpaRepository<ContestQuestion, Long> {

    List<ContestQuestion> findByContestIdOrderByOrderIndex(Long contestId);

    long countByContestId(Long contestId);

}
