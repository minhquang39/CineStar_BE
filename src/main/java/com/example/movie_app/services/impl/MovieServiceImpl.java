package com.example.movie_app.services.impl;

import com.cloudinary.utils.ObjectUtils;
import com.example.movie_app.configurations.CloudinaryConfig;
import com.example.movie_app.dtos.requests.MovieRequest;
import com.example.movie_app.dtos.responses.MovieResponse;
import com.example.movie_app.exceptions.AppException;
import com.example.movie_app.exceptions.ErrorCode;
import com.example.movie_app.exceptions.NotFoundException;
import com.example.movie_app.mappers.MovieMapper;
import com.example.movie_app.models.Movie;
import com.example.movie_app.repositories.MovieRepository;
import com.example.movie_app.services.MovieService;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final CloudinaryConfig cloudinaryConfig;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


    @Override
    public MovieResponse createMovie(MovieRequest movieRequest) throws IOException {
        Slugify slg = Slugify.builder().build();
        String slug = slg.slugify(movieRequest.getTitle());
        if(movieRepository.existsBySlug(slug)){
            throw new AppException(ErrorCode.MOVIE_EXIST);
        }


        String posterUrl = uploadImage(movieRequest.getPosterImage());
        Movie movie = movieMapper.toMovie(movieRequest);
        movie.setPosterUrl(posterUrl);
        movieRepository.save(movie);

    return movieMapper.toMovieResponse(movie);
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movie -> movieMapper.toMovieResponse(movie)).toList();
    }

    @Transactional
    @Override
    public MovieResponse deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new NotFoundException("Movie not found"));
        movieRepository.delete(movie);
        return movieMapper.toMovieResponse(movie);
    }

    @Override
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) throws IOException {

        System.out.println("Edit movie endpoint");
        System.out.println("Edit movie id: " + movieId);

        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new NotFoundException("Movie not found"));

        Slugify slg = Slugify.builder().build();
        String slug = slg.slugify(movieRequest.getTitle());
        if(movieRepository.existsBySlug(slug)){
            throw new AppException(ErrorCode.MOVIE_EXIST);
        }

        if(!movieRequest.getPosterImage().isEmpty() && movieRequest.getPosterImage()!=null){
            String posterUrl = uploadImage(movieRequest.getPosterImage());
            movie.setPosterUrl(posterUrl);
        }


            movie = movieMapper.toMovie(movieRequest);
            movieRepository.save(movie);

        return movieMapper.toMovieResponse(movie);
    }


    private String uploadImage(MultipartFile file) throws IOException {
        if(file==null || file.isEmpty()) {
            return null;
        }

        Map<String,Object> uploadResult = cloudinaryConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }
}
