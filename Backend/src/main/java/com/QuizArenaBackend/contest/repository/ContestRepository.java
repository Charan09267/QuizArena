package com.QuizArenaBackend.contest.repository;

import com.QuizArenaBackend.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {


}
