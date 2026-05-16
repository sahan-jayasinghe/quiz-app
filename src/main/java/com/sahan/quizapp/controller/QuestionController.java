package com.sahan.quizapp.controller;

import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.mapper.QuestionMapper;
import com.sahan.quizapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/allquestions")
    public ResponseEntity<List<QuestionDto>> getAllQuestions(){
        ResponseEntity<List<QuestionDto>> response = questionService.getAllQuestions();
        return response;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDto>> getQuestionByCategory(@PathVariable String category){
        ResponseEntity<List<QuestionDto>> response = questionService.getQuestionByCategory(category);
        return response;
    }

    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionDto questionDto){
        return questionService.addQuestion(QuestionMapper.toModel(questionDto));
    }

    @PutMapping("update")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionDto questionDto){
        return questionService.updateQuestion(QuestionMapper.toModel(questionDto));
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteQuestion(@RequestBody QuestionDto questionDto){
        return questionService.deleteQuestion(QuestionMapper.toModel(questionDto));
    }

}
