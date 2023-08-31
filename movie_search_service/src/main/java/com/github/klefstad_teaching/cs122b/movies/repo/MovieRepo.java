package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class MovieRepo
{
    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
        this.template = template;
        this.objectMapper = objectMapper;
    }

//    public Movie testGettingTemplate(int id)
//    {
//        Movie findMovieFromId = this.template.queryForObject("SELECT m.id, title, year, p.name, rating, backdrop_path, poster_path, hidden " +
//                " FROM movies.movie m " +
//                " JOIN movies.person p ON m.director_id = p.id " +
//                " WHERE m.id = :id ",
//                new MapSqlParameterSource()
//                        .addValue("id", id, Types.INTEGER),
//                (rs, rowNum) ->
//                        new Movie()
//                                .setDirector(rs.getString("p.name"))
//                                .setHidden(false)
//                                .setPosterPath("/or06FN3Dka5tukK1e9sl16pB3iy.jpg")
//                                .setRating(90.0)
//                                .setYear(2018)
//                                .setTitle(rs.getString("title"))
//                                .setId(4154796L)
//                                .setBackdropPath("/or06FN3Dka5tukK1e9sl16pB3iy.jpg")
//                );
//        return findMovieFromId;
//    }
    public String getDirectorNameFromId(int directorId)
    {
        PersonDetail findDir = this.template.queryForObject("SELECT p.name " +
                        "FROM movies.person p " +
                        "WHERE p.id = :dirId",
                new MapSqlParameterSource()
                        .addValue("dirId", directorId, Types.INTEGER),
                (rs, rowNum) ->
                        new PersonDetail()
                                .setName(rs.getString("p.name"))); //p.name?
        return findDir.getName();

    }

}
