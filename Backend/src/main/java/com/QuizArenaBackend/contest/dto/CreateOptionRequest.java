package com.QuizArenaBackend.contest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOptionRequest {

    private String optionText;

    private Boolean isCorrect;

}
