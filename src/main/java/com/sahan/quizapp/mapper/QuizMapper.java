package com.sahan.quizapp.mapper;

import com.sahan.quizapp.dto.QuizDto;
import com.sahan.quizapp.model.Quiz;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuizMapper {

    public static QuizDto toDto(Quiz quiz) {
        if (quiz == null) return null;
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setQuestions(QuestionMapper.toDtoList(quiz.getQuestions()));
        return dto;
    }

    public static Quiz toModel(QuizDto dto) {
        if (dto == null) return null;
        Quiz q = new Quiz();
        q.setId(dto.getId());
        q.setTitle(dto.getTitle());
        q.setQuestions(QuestionMapper.toModelList(dto.getQuestions()));
        return q;
    }

    public static List<QuizDto> toDtoList(List<Quiz> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(QuizMapper::toDto).collect(Collectors.toList());
    }

    public static List<Quiz> toModelList(List<QuizDto> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(QuizMapper::toModel).collect(Collectors.toList());
    }
}
