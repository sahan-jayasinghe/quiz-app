package com.sahan.quizapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto implements Serializable {
    private Integer id;
    
    @NotBlank(message = "Question title is required")
    @Size(min = 5, max = 255, message = "Question title must be between 5 and 255 characters")
    private String questionTitle;
    
    @NotBlank(message = "Option 1 is required")
    @Size(min = 1, max = 100, message = "Option 1 must be between 1 and 100 characters")
    private String option1;
    
    @NotBlank(message = "Option 2 is required")
    @Size(min = 1, max = 100, message = "Option 2 must be between 1 and 100 characters")
    private String option2;
    
    @NotBlank(message = "Option 3 is required")
    @Size(min = 1, max = 100, message = "Option 3 must be between 1 and 100 characters")
    private String option3;
    
    @NotBlank(message = "Option 4 is required")
    @Size(min = 1, max = 100, message = "Option 4 must be between 1 and 100 characters")
    private String option4;
    
    @NotBlank(message = "Right answer is required")
    private String rightAnswer;
    
    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;
    
    @NotBlank(message = "Category is required")
    private String category;
}
