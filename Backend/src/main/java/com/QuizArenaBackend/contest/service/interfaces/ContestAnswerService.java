package com.QuizArenaBackend.contest.service.interfaces;

public interface ContestAnswerService {

    void saveAnswer(
            Long attemptId,
            Long questionId,
            Long optionId
    );

//    List<ContestAnswer> getAnswers(Long attemptId);
}
