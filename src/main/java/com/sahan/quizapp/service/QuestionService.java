package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.QuestionDao;
import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.mapper.QuestionMapper;
import com.sahan.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;


    public ResponseEntity<List<QuestionDto>> getAllQuestions(){
        try {
            List<Question> questions = questionDao.findAll();
            List<QuestionDto> dtos = QuestionMapper.toDtoList(questions);
            return new ResponseEntity(dtos, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionDto>> getQuestionByCategory(String category){
        List<Question> questions = questionDao.findByCategory(category);
        List<QuestionDto> dtos = QuestionMapper.toDtoList(questions);
        return new ResponseEntity(dtos, HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    public ResponseEntity<String> updateQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteQuestion(Question question) {
        questionDao.delete(question);
        return new ResponseEntity("success", HttpStatus.OK);
    }
}
