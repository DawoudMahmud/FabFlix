package com.github.klefstad_teaching.cs122b.movies.model.request;

public class MovieGetByMovieIdRequest {
    private Long movieId;

    public Long getMovieId() {
        return movieId;
    }

    public MovieGetByMovieIdRequest setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }
}
