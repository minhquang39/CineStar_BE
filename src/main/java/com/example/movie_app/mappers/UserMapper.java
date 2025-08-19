package com.example.movie_app.mappers;

import com.example.movie_app.dtos.responses.UserResponse;
import com.example.movie_app.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .fullname(user.getFullname())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
