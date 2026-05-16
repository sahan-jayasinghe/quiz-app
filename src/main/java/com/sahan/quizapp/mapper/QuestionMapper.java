package com.sahan.quizapp.mapper;

import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.model.Question;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuestionMapper {

    public static QuestionDto toDto(Question question) {
        if (question == null) return null;
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionTitle(question.getQuestionTitle());
        dto.setOption1(question.getOption1());
        dto.setOption2(question.getOption2());
        dto.setOption3(question.getOption3());
        dto.setOption4(question.getOption4());
        return dto;
    }

    public static Question toModel(QuestionDto dto) {
        if (dto == null) return null;
        Question q = new Question();
        q.setId(dto.getId());
        q.setQuestionTitle(dto.getQuestionTitle());
        q.setOption1(dto.getOption1());
        q.setOption2(dto.getOption2());
        q.setOption3(dto.getOption3());
        q.setOption4(dto.getOption4());
        return q;
    }

    public static List<QuestionDto> toDtoList(List<Question> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(QuestionMapper::toDto).collect(Collectors.toList());
    }

    public static List<Question> toModelList(List<QuestionDto> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(QuestionMapper::toModel).collect(Collectors.toList());
    }
}
