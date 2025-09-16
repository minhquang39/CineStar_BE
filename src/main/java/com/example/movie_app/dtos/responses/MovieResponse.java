package com.example.movie_app.dtos.responses;

import com.example.movie_app.utils.MovieStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
    Long id;

    String title;

    String description;

    String director;

    String country;

    int duration;

    LocalDate releaseDate;

    String posterUrl;

    String trailerUrl;

    String slug;

    boolean isActive;

    MovieStatus status;

    float rating;
}
