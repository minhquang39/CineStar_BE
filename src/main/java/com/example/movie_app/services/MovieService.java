package com.example.movie_app.services;

import com.example.movie_app.dtos.requests.MovieRequest;
import com.example.movie_app.dtos.responses.MovieResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieResponse createMovie(MovieRequest movieRequest) throws IOException;
    List<MovieResponse> getAllMovies();
    MovieResponse deleteMovie(Long movieId);
    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) throws IOException;
}
