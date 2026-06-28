package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.CreateQuestionRequest;
import com.QuizArenaBackend.contest.dto.QuestionResponse;
import com.QuizArenaBackend.contest.entity.Contest;
import com.QuizArenaBackend.contest.entity.ContestOption;
import com.QuizArenaBackend.contest.entity.ContestQuestion;
import com.QuizArenaBackend.contest.repository.ContestQuestionRepository;
import com.QuizArenaBackend.contest.repository.ContestRepository;
import com.QuizArenaBackend.contest.service.Mappers.QuestionMapper;
import com.QuizArenaBackend.contest.service.interfaces.ContestQuestionService;
import org.springframework.transaction.annotation.Transactional;import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestQuestionServiceImpl implements ContestQuestionService {

    private final ContestQuestionRepository questionRepository;
    private final ContestRepository contestRepository;
//    private final ContestOptionRepository optionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionResponse addQuestion(Long contestId,
                                        CreateQuestionRequest request) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        ContestQuestion question = ContestQuestion.builder()
                .contest(contest)
                .questionText(request.getQuestionText())
                .questionType(request.getQuestionType())
                .marks(request.getMarks())
                .explanation(request.getExplanation())
                .orderIndex(request.getOrderIndex())
                .build();

        List<ContestOption> options = request.getOptions()
                .stream()
                .map(optionRequest ->
                        ContestOption.builder()
                                .question(question)
                                .optionText(optionRequest.getOptionText())
                                .isCorrect(optionRequest.getIsCorrect())
                                .build()
                )
                .toList();

        question.getOptions().addAll(options);

        ContestQuestion savedQuestion =
                questionRepository.save(question);

        return questionMapper.toResponse(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(Long contestId) {

        List<ContestQuestion> questions =
                questionRepository.findByContestIdOrderByOrderIndex(contestId);

        return questions.stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @Override
    public QuestionResponse updateQuestion(Long questionId,
                                           CreateQuestionRequest request) {

        ContestQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found"));

        question.setQuestionText(request.getQuestionText());
        question.setQuestionType(request.getQuestionType());
        question.setMarks(request.getMarks());
        question.setExplanation(request.getExplanation());
        question.setOrderIndex(request.getOrderIndex());

    /*
        Remove previous options
     */

        question.getOptions().clear();

    /*
        Add new options
     */

        List<ContestOption> newOptions = request.getOptions()
                .stream()
                .map(optionRequest ->
                        ContestOption.builder()
                                .question(question)
                                .optionText(optionRequest.getOptionText())
                                .isCorrect(optionRequest.getIsCorrect())
                                .build()
                )
                .toList();

        question.getOptions().addAll(newOptions);

        ContestQuestion updatedQuestion =
                questionRepository.save(question);

        return questionMapper.toResponse(updatedQuestion);
    }


    @Override
    public void deleteQuestion(Long questionId) {

        ContestQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found"));

        questionRepository.delete(question);

    }

}
