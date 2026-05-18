package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.UserDao;
import com.sahan.quizapp.dto.AuthRequest;
import com.sahan.quizapp.dto.AuthResponse;
import com.sahan.quizapp.exception.ResourceNotFoundException;
import com.sahan.quizapp.model.User;
import com.sahan.quizapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder encoder;

    public String register(AuthRequest req) {
        if (userDao.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        userDao.save(u);
        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest req) {
        User u = userDao.findByUsername(req.getUsername());
        if (u == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtTokenProvider.generateToken(u.getUsername());
        return new AuthResponse(token, u.getUsername());
    }

}
