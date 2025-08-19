package com.example.movie_app.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequest {
    private String email;
    private String username;
    private String password;
}
