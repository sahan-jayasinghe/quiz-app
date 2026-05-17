package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.QuestionDao;
import com.sahan.quizapp.dao.QuizDao;
import com.sahan.quizapp.dto.QuestionDto;
import com.sahan.quizapp.dto.QuestionGetDto;
import com.sahan.quizapp.dto.QuizDto;
import com.sahan.quizapp.mapper.QuestionMapper;
import com.sahan.quizapp.mapper.QuizMapper;
import com.sahan.quizapp.exception.ResourceNotFoundException;
import com.sahan.quizapp.model.Question;
import com.sahan.quizapp.model.Quiz;
import com.sahan.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {


    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public String createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return "success";
    }

    public List<QuestionGetDto> getQuizQuestions(Integer id){
        Optional<Quiz> quiz = quizDao.findById(id);
        if (quiz.isEmpty()) {
            throw new ResourceNotFoundException("Quiz not found with id: " + id);
        }

        List<Question> questionFromDb = quiz.get().getQuestions();
        return QuestionMapper.toGetDtoList(questionFromDb);
    }

    public Integer submitQuiz(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
        List<Question> questions = quiz.getQuestions();

        int num = 0;
        int marks = 0;
        for (Response response : responses){
            if(response.getResponse().equals(questions.get(num).getRightAnswer()))
                marks++;

            num++;
        }

        return marks;
    }

    public QuizDto getQuizById(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        if (quiz.isPresent()) {
            return QuizMapper.toDto(quiz.get());
        }
        throw new ResourceNotFoundException("Quiz not found with id: " + id);
    }

}