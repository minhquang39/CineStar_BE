package com.example.movie_app.repositories;

import com.example.movie_app.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsBySlug(String slug);

    @Transactional
    @Modifying
    @Query("delete from Movie m")
    int deleteFirstBy();
}
