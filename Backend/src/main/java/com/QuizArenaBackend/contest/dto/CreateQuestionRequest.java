package com.QuizArenaBackend.contest.dto;

import com.QuizArenaBackend.contest.entity.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuestionRequest {

    @NotBlank(message = "Question cannot be empty")
    private String questionText;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    @NotNull(message = "Marks are required")
    @Positive(message = "Marks must be greater than 0")
    private Integer marks;

    @Size(max = 2000)
    private String explanation;

    @NotNull(message = "Order index is required")
    @Positive(message = "Order index must be positive")
    private Integer orderIndex;

    @NotEmpty(message = "Question must contain options")
    @Valid
    private List<CreateOptionRequest> options;

}
