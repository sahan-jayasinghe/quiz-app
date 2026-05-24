package com.sahan.quizapp.controller;

import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.mapper.QuestionMapper;
import com.sahan.quizapp.response.ApiResponse;
import com.sahan.quizapp.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/allquestions")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getAllQuestions(){
        List<QuestionDto> questions = questionService.getAllQuestions();
        ApiResponse<List<QuestionDto>> apiResponse = new ApiResponse<>(true, questions, "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getQuestionByCategory(@PathVariable String category){
        List<QuestionDto> questions = questionService.getQuestionByCategory(category);
        ApiResponse<List<QuestionDto>> apiResponse = new ApiResponse<>(true, questions, "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("add")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<String>> addQuestion(@Valid @RequestBody QuestionDto questionDto){
        String message = questionService.addQuestion(QuestionMapper.toModel(questionDto));
        ApiResponse<String> apiResponse = new ApiResponse<>(true, null, message, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("update")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<String>> updateQuestion(@Valid @RequestBody QuestionDto questionDto){
        String message = questionService.updateQuestion(QuestionMapper.toModel(questionDto));
        ApiResponse<String> apiResponse = new ApiResponse<>(true, null, message, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<String>> deleteQuestion(@Valid @RequestBody QuestionDto questionDto){
        String message = questionService.deleteQuestion(QuestionMapper.toModel(questionDto));
        ApiResponse<String> apiResponse = new ApiResponse<>(true, null, message, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
