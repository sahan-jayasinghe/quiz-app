package com.sahan.quizapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Response {
    public Integer id;
    public String response;
}
