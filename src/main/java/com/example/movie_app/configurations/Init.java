package com.example.movie_app.configurations;

import com.example.movie_app.models.User;
import com.example.movie_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class Init {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean(name="adminInitializer")
    CommandLineRunner init(){
        return args -> {
            if(userRepository.findUserByUsername("admin").isEmpty()){
                User admin  = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
            }
        };
    }
}
