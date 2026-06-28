package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.contest.entity.ContestOption;
import com.QuizArenaBackend.contest.entity.ContestQuestion;
import com.QuizArenaBackend.contest.entity.ContestQuestionAttempt;
import com.QuizArenaBackend.contest.repository.ContestAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestQuestionAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestQuestionRepository;
import com.QuizArenaBackend.contest.service.interfaces.ContestEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestEvaluationServiceImpl
        implements ContestEvaluationService {

    private final ContestQuestionRepository questionRepository;
    private final ContestQuestionAttemptRepository questionAttemptRepository;
    private final ContestAttemptRepository attemptRepository;

    @Override
    @Transactional
    public void evaluateAttempt(ContestAttempt attempt) {

        List<ContestQuestion> questions =
                questionRepository.findByContestIdOrderByOrderIndex(
                        attempt.getContest().getId());

        BigDecimal totalScore = BigDecimal.ZERO;

        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        for (ContestQuestion question : questions) {

            Optional<ContestQuestionAttempt> answerOptional =
                    questionAttemptRepository
                            .findByContestAttemptIdAndQuestionId(
                                    attempt.getId(),
                                    question.getId());

            if (answerOptional.isEmpty()) {
                unanswered++;
                continue;
            }

            ContestQuestionAttempt answer =
                    answerOptional.get();

            ContestOption selectedOption =
                    answer.getSelectedOption();

            if (selectedOption != null &&
                    Boolean.TRUE.equals(selectedOption.getIsCorrect())) {

                answer.setIsCorrect(true);

                BigDecimal marks =
                        BigDecimal.valueOf(question.getMarks());

                answer.setMarksAwarded(marks);

                totalScore = totalScore.add(marks);

                correct++;

            } else {

                answer.setIsCorrect(false);
                answer.setMarksAwarded(BigDecimal.ZERO);

                wrong++;
            }
        }

        attempt.setScore(totalScore);
        attempt.setCorrectAnswers(correct);
        attempt.setWrongAnswers(wrong);
        attempt.setUnansweredQuestions(unanswered);
        attempt.setTotalQuestions(questions.size());

        attemptRepository.save(attempt);
    }
}