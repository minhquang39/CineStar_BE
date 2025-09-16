package com.example.movie_app.dtos.requests;

import com.example.movie_app.utils.MovieStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {
    String title;
    String description;
    String director;
    String country;
    int duration;
    LocalDate releaseDate;
    MultipartFile posterImage;
    String posterUrl;
    String trailerUrl;
    MovieStatus status;
    boolean isActive;
}
