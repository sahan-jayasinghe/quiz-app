package com.sahan.quizapp.controller;

import com.sahan.quizapp.dto.AuthRequest;
import com.sahan.quizapp.dto.AuthResponse;
import com.sahan.quizapp.response.ApiResponse;
import com.sahan.quizapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody AuthRequest req) {
        String msg = authService.register(req);
        ApiResponse<String> api = new ApiResponse<>(true, msg, "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        AuthResponse authResp = authService.login(req);
        ApiResponse<AuthResponse> api = new ApiResponse<>(true, authResp, "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}
