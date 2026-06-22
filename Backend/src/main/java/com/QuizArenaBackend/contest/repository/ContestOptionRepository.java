package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.ContestOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestOptionRepository
        extends JpaRepository<ContestOption, Long> {

    List<ContestOption> findByQuestionId(Long questionId);

}
