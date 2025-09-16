package com.example.movie_app.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String email;
    private String username;
    private String token;
    private String role;
}
