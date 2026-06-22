package com.QuizArenaBackend.contest.service;

import com.QuizArenaBackend.contest.dto.CreateQuestionRequest;
import com.QuizArenaBackend.contest.dto.QuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestQuestionServiceImpl implements ContestQuestionService{
    @Override
    public QuestionResponse addQuestion(Long contestId, CreateQuestionRequest request) {
        return null;
    }

    @Override
    public List<QuestionResponse> getQuestions(Long contestId) {
        return List.of();
    }

    @Override
    public QuestionResponse updateQuestion(Long questionId, CreateQuestionRequest request) {
        return null;
    }

    @Override
    public void deleteQuestion(Long questionId) {

    }
}
