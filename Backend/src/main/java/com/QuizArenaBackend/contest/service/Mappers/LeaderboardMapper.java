package com.QuizArenaBackend.contest.service.Mappers;

import com.QuizArenaBackend.contest.dto.LeaderboardResponse;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.user.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardMapper {

    public LeaderboardResponse toResponse(
            ContestAttempt attempt,
            Integer rank) {

        UserEntity user =
                attempt.getParticipant().getUser();

        return LeaderboardResponse.builder()
                .rank(rank)
                .userId(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .score(attempt.getScore())
                .correctAnswers(
                        attempt.getCorrectAnswers())
                .wrongAnswers(
                        attempt.getWrongAnswers())
                .unansweredQuestions(
                        attempt.getUnansweredQuestions())
                .timeTakenSeconds(
                        attempt.getTimeTakenSeconds())
                .submittedAt(
                        attempt.getSubmittedAt())
                .build();
    }
}
