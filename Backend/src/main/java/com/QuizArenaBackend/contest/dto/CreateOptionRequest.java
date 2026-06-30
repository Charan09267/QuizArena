package com.QuizArenaBackend.contest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOptionRequest {

    @NotBlank(message = "Option text cannot be empty")
    private String optionText;

    @NotNull(message = "Correct flag is required")
    private Boolean isCorrect;

}
