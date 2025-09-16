package com.example.movie_app.controllers;

import com.cloudinary.Api;
import com.example.movie_app.dtos.ApiResponse;
import com.example.movie_app.dtos.requests.MovieRequest;
import com.example.movie_app.dtos.responses.MovieResponse;
import com.example.movie_app.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(@ModelAttribute MovieRequest movieRequest){
        try{
            MovieResponse movieResponse = movieService.createMovie(movieRequest);
            return ResponseEntity.status(200).body(ApiResponse.success(movieResponse,"Create Movie Successfully"));
        } catch(Exception e){
            return ResponseEntity.status(404).body(ApiResponse.error(404,"Failed to create movie" + e.getMessage()));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getAllMovie(){
        try{
            List<MovieResponse> movieResponses = movieService.getAllMovies();
            return ResponseEntity.status(200).body(ApiResponse.success(movieResponses,"Get all movies successfully"));
        } catch (Exception e){
            return ResponseEntity.status(404).body(ApiResponse.error(404,"Failed to get all movies :" + e.getMessage()));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> deleteMovie(@PathVariable Long id){
        try{
            return ResponseEntity.status(200).body(ApiResponse.success(movieService.deleteMovie(id), "Delete Movie Successfully"));
        } catch (Exception e){
            return ResponseEntity.status(404).body(ApiResponse.error(404,"Failed to delete movie " + e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value="/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(@PathVariable Long id, @ModelAttribute MovieRequest movieRequest){
        try{
            return ResponseEntity.status(200).body(ApiResponse.success(movieService.updateMovie(id, movieRequest), "Update Movie Successfully"));
        }catch (Exception e){
            return ResponseEntity.status(404).body(ApiResponse.error(404,"Failed to update movie " + e.getMessage()));
        }
    }
}
