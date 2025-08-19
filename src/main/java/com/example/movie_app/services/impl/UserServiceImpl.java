package com.example.movie_app.services.impl;

import com.example.movie_app.dtos.requests.LoginRequest;
import com.example.movie_app.dtos.requests.UserRequest;
import com.example.movie_app.dtos.requests.UserUpdateRequest;
import com.example.movie_app.dtos.responses.LoginResponse;
import com.example.movie_app.dtos.responses.UserResponse;
import com.example.movie_app.exceptions.AppException;
import com.example.movie_app.exceptions.ErrorCode;
import com.example.movie_app.exceptions.UserNotFoundException;
import com.example.movie_app.exceptions.UsernameAlreadyExistException;
import com.example.movie_app.mappers.UserMapper;
import com.example.movie_app.models.User;
import com.example.movie_app.repositories.UserRepository;
import com.example.movie_app.services.JwtService;
import com.example.movie_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new UsernameAlreadyExistException(userRequest.getUsername());
         }

        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXIST);
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setFullname(userRequest.getFullname());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        return new UserResponse().builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullname(savedUser.getFullname())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(()-> new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new AppException(ErrorCode.INVALID_USER);
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                user.getEmail(),
                user.getUsername(),
                token
        );
    }

    @Override
    public UserResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        System.out.println("username: " + username);
        System.out.println("Context: " + context.getAuthentication().getAuthorities());
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));
        return new UserResponse().builder()
                .username(username)
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));
        if(userUpdateRequest.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }
        if(userUpdateRequest.getFullname()!=null){
            user.setFullname(userUpdateRequest.getFullname());
        }

        user = userRepository.save(user);
        return new UserResponse().builder()
                .username(username)
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user->userMapper.toUserResponse(user))
                .toList();
    }

    @Override
    public UserResponse deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        if(user.getRole().equals("ROLE_ADMIN")){
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }
        userRepository.delete(user);
        return new UserResponse().builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
