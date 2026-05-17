package com.sahan.quizapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Response {
    @NotNull(message = "Question ID is required")
    public Integer id;
    
    @NotBlank(message = "Response cannot be blank")
    public String response;
}
