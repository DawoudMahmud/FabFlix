package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;

import java.util.List;

public class MovieGetByMovieIdResponse extends ResponseModel<MovieGetByMovieIdResponse> {
    private MovieDetail movie;

    public MovieDetail getMovie() {
        return movie;
    }

    public MovieGetByMovieIdResponse setMovie(MovieDetail movie) {
        this.movie = movie;
        return this;
    }

    private List<Genre> genres;
    private List<Person> persons;



    public List<Genre> getGenres() {
        return genres;
    }

    public MovieGetByMovieIdResponse setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public MovieGetByMovieIdResponse setPersons(List<Person> persons) {
        this.persons = persons;
        return this;
    }
}
