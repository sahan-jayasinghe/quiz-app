package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.QuestionDao;
import com.sahan.quizapp.dao.QuizDao;
import com.sahan.quizapp.dto.QuizDto;
import com.sahan.quizapp.mapper.QuizMapper;
import com.sahan.quizapp.model.Question;
import com.sahan.quizapp.model.QuestionWrapper;
import com.sahan.quizapp.model.Quiz;
import com.sahan.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {


    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id){
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionFromDb = quiz.get().getQuestions();
        List<QuestionWrapper> questionForUser = new ArrayList<>();
        for(Question q : questionFromDb){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionForUser.add(qw);
        }

        return new ResponseEntity<>(questionForUser,HttpStatus.OK);
    };

    public ResponseEntity<Integer> submitQuiz(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();

        int num = 0;
        int marks = 0;
        for (Response response : responses){
            if(response.getResponse().equals(questions.get(num).getRightAnswer()))
                marks++;

            num++;
        }

        return new ResponseEntity<>(marks, HttpStatus.OK);
    }

    public ResponseEntity<QuizDto> getQuizById(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        if (quiz.isPresent()) {
            QuizDto dto = QuizMapper.toDto(quiz.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();    }

}