package com.example.movie_app.services;

import com.example.movie_app.dtos.requests.LoginRequest;
import com.example.movie_app.dtos.requests.UserRequest;
import com.example.movie_app.dtos.requests.UserUpdateRequest;
import com.example.movie_app.dtos.responses.LoginResponse;
import com.example.movie_app.dtos.responses.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
    UserResponse getInfo();
    UserResponse updateUser(UserUpdateRequest userUpdateRequest);

    List<UserResponse> getAllUsers();

    UserResponse deleteUser(Long id);

    LoginResponse refreshToken(String refreshToken);

    Boolean logout();
}
