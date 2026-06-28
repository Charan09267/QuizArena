package com.QuizArenaBackend.contest.service.interfaces;

import com.QuizArenaBackend.contest.dto.QuestionAttemptResponse;
import com.QuizArenaBackend.contest.dto.SaveAnswerRequest;

import java.util.List;

public interface ContestQuestionAttemptService {

    QuestionAttemptResponse saveAnswer(
            Long attemptId,
            Long questionId,
            SaveAnswerRequest request
    );

    List<QuestionAttemptResponse> getAnswers(
            Long attemptId
    );

}
