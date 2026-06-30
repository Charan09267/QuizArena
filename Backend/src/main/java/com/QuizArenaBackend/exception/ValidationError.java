package com.QuizArenaBackend.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private Map<String,String> errors;

    private String path;
}
