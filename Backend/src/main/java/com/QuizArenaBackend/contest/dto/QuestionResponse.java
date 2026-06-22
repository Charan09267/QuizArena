package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class QuestionResponse {

    private Long id;

    private String questionText;

    private QuestionType questionType;

    private Integer marks;

    private String explanation;

    private Integer orderIndex;

    private List<OptionResponse> options;

}
