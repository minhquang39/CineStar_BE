package com.example.movie_app.models;

import com.example.movie_app.utils.MovieStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name="movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="title")
    String title;
    @Column(name="description")
    String description;
    @Column(name="director")
    String director;
    @Column(name="country")
    String country;
    @Column(name="duration")
    int duration;
    @Column(name="releaseDate")
    LocalDate releaseDate;
    @Column(name="posterUrl")
    String posterUrl;
    @Column(name="trailerUrl")
    String trailerUrl;
    @Column(name="slug",unique = true)
    String slug;
    @Column(name="is_active")
    boolean isActive;
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    MovieStatus status;
    @Column(name="rating")
    float rating;
}
