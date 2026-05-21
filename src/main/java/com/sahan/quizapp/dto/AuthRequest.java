package com.sahan.quizapp.dto;

import com.sahan.quizapp.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private Role role = Role.STUDENT;

}
