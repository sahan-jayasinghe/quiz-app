package com.sahan.quizapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private Integer id;
    
    @NotBlank(message = "Quiz title is required")
    @Size(min = 3, max = 100, message = "Quiz title must be between 3 and 100 characters")
    private String title;
    
    @NotEmpty(message = "At least one question is required")
    @Valid
    private List<QuestionDto> questions;
}
