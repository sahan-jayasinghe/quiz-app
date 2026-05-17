package com.sahan.quizapp.controller;

import com.sahan.quizapp.dto.QuestionGetDto;
import com.sahan.quizapp.dto.QuizDto;
import com.sahan.quizapp.model.Response;
import com.sahan.quizapp.response.ApiResponse;
import com.sahan.quizapp.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@Validated
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createQuiz(@RequestParam @NotBlank String category, @RequestParam @Min(1) int numQ, @RequestParam @NotBlank String title) {
        String message = quizService.createQuiz(category, numQ, title);
        ApiResponse<String> api = new ApiResponse<>(true, null, message, null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<List<QuestionGetDto>>> getQuizQuestions(@PathVariable Integer id) {
        List<QuestionGetDto> questions = quizService.getQuizQuestions(id);
        ApiResponse<List<QuestionGetDto>> api = new ApiResponse<>(true, questions, "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<ApiResponse<Integer>> submitQuiz(@PathVariable Integer id, @Valid @RequestBody List<Response> responses) {
        Integer marks = quizService.submitQuiz(id, responses);
        ApiResponse<Integer> api = new ApiResponse<>(true, marks, "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizDto>> getQuizById(@PathVariable Integer id) {
        QuizDto quiz = quizService.getQuizById(id);
        ApiResponse<QuizDto> api = new ApiResponse<>(true, quiz, "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
}





