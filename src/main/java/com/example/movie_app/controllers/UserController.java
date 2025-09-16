package com.example.movie_app.controllers;

import com.example.movie_app.dtos.ApiResponse;
import com.example.movie_app.dtos.requests.LoginRequest;
import com.example.movie_app.dtos.requests.UserRequest;
import com.example.movie_app.dtos.requests.UserUpdateRequest;
import com.example.movie_app.dtos.responses.LoginResponse;
import com.example.movie_app.dtos.responses.UserResponse;
import com.example.movie_app.services.JwtService;
import com.example.movie_app.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody UserRequest userRequest){
        UserResponse response = userService.createUser(userRequest);
        return  ResponseEntity.status(200).body(ApiResponse.success(response,"Create user successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = userService.loginUser(loginRequest);

        String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());

        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7*24*60*60*1000);
        response.addCookie(cookie);

        return ResponseEntity.status(200).body(ApiResponse.success(loginResponse,"Login successfully!"));
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable Long id){
        return ResponseEntity.status(200).body(ApiResponse.success(userService.deleteUser(id), "Delete user successfully!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@CookieValue(value="refreshToken",required=false) String refreshToken){
        return ResponseEntity.status(200).body(ApiResponse.success(userService.refreshToken(refreshToken), "Refresh token successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(200).body(ApiResponse.success(null,"Logout successfully!"));
    }
}
