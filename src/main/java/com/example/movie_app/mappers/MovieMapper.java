package com.example.movie_app.mappers;

import com.example.movie_app.dtos.requests.MovieRequest;
import com.example.movie_app.dtos.responses.MovieResponse;
import com.example.movie_app.models.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface MovieMapper {
    MovieResponse toMovieResponse(Movie movie);
    Movie toMovie(MovieRequest movieRequest);
}
