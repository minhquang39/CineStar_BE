package com.example.movie_app.controllers;

import com.example.movie_app.dtos.ApiResponse;
import com.example.movie_app.dtos.requests.LoginRequest;
import com.example.movie_app.dtos.requests.UserRequest;
import com.example.movie_app.dtos.requests.UserUpdateRequest;
import com.example.movie_app.dtos.responses.LoginResponse;
import com.example.movie_app.dtos.responses.UserResponse;
import com.example.movie_app.services.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody UserRequest userRequest){
        UserResponse response = userService.createUser(userRequest);
        return  ResponseEntity.status(200).body(ApiResponse.success(response,"Create user successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest loginRequest){
        LoginResponse response = userService.loginUser(loginRequest);
        return ResponseEntity.status(200).body(ApiResponse.success(response,"Login successfully!"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(){
        UserResponse response = userService.getInfo();
        return ResponseEntity.status(200).body(ApiResponse.success(response,"Get info user successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        UserResponse response = userService.updateUser(userUpdateRequest);
        return ResponseEntity.status(200).body(ApiResponse.success(response,"Update user successfully!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<UserResponse> userResponseList = userService.getAllUsers();
            return ResponseEntity.ok(
                    ApiResponse.success(userResponseList, "Get all users successfully")
            );
        } catch (Exception e) {
            log.error("Get all user error", e);
            return ResponseEntity.status(404).body(ApiResponse.error(404, "Get all user error"));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable Long id){
        return ResponseEntity.status(200).body(ApiResponse.success(userService.deleteUser(id), "Delete user successfully!"));
    }
}
