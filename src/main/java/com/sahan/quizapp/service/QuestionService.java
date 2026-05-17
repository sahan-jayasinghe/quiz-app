package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.QuestionDao;
import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.mapper.QuestionMapper;
import com.sahan.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;


    public List<QuestionDto> getAllQuestions(){
        List<Question> questions = questionDao.findAll();
        return QuestionMapper.toDtoList(questions);
    }

    public List<QuestionDto> getQuestionByCategory(String category){
        List<Question> questions = questionDao.findByCategory(category);
        return QuestionMapper.toDtoList(questions);
    }

    public String addQuestion(Question question) {
        questionDao.save(question);
        return "success";
    }

    public String updateQuestion(Question question) {
        questionDao.save(question);
        return "success";
    }

    public String deleteQuestion(Question question) {
        questionDao.delete(question);
        return "success";
    }
}
