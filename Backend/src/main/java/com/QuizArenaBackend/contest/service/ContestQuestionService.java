package com.QuizArenaBackend.contest.service;

import com.QuizArenaBackend.contest.dto.CreateQuestionRequest;
import com.QuizArenaBackend.contest.dto.QuestionResponse;

import java.util.List;

public interface ContestQuestionService {

    QuestionResponse addQuestion(
            Long contestId,
            CreateQuestionRequest request);

    List<QuestionResponse> getQuestions(
            Long contestId);

    QuestionResponse updateQuestion(
            Long questionId,
            CreateQuestionRequest request);

    void deleteQuestion(Long questionId);

}
