package com.sahan.quizapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private Integer id;
    private String title;
    private List<QuestionDto> questions;
}
