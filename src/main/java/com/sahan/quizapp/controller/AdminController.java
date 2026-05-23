package com.sahan.quizapp.controller;

import com.sahan.quizapp.dao.UserDao;
import com.sahan.quizapp.dto.AuthRequest;
import com.sahan.quizapp.model.Role;
import com.sahan.quizapp.model.User;
import com.sahan.quizapp.response.ApiResponse;
import com.sahan.quizapp.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/setup")
    public ResponseEntity<ApiResponse<String>> setupAdmin(@Valid @RequestBody AuthRequest req) {
        if (userDao.count() > 0) {
            ApiResponse<String> api = new ApiResponse<>(false, null, "setup already completed", null);
            return new ResponseEntity<>(api, HttpStatus.BAD_REQUEST);
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        u.setEnabled(true);
        u.setRole(Role.ADMIN);
        userDao.save(u);
        ApiResponse<String> api = new ApiResponse<>(true, "admin created", "success", null);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateUserRole(@PathVariable Integer id, @RequestParam String role) {
        User u = userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try {
            Role r = Role.valueOf(role);
            u.setRole(r);
            userDao.save(u);
            ApiResponse<String> api = new ApiResponse<>(true, "role updated", "success", null);
            return new ResponseEntity<>(api, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> api = new ApiResponse<>(false, null, "invalid role", null);
            return new ResponseEntity<>(api, HttpStatus.BAD_REQUEST);
        }
    }

}
