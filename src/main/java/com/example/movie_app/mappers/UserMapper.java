package com.example.movie_app.mappers;

import com.example.movie_app.dtos.responses.UserResponse;
import com.example.movie_app.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
