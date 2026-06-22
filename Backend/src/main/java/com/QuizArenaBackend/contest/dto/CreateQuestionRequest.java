package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuestionRequest {

    private String questionText;

    private QuestionType questionType;

    private Integer marks;

    private String explanation;

    private Integer orderIndex;

    private List<CreateOptionRequest> options;

}
