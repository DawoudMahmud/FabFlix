package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;
import com.github.klefstad_teaching.cs122b.movies.model.request.MovieGetByMovieIdRequest;
import com.github.klefstad_teaching.cs122b.movies.model.request.PersonGetByPersonIdRequest;
import com.github.klefstad_teaching.cs122b.movies.model.request.PersonSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieGetByMovieIdResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonGetByPersonIdResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@RestController
public class PersonController
{
    private final MovieRepo repo;

    @Autowired
    public PersonController(MovieRepo repo)
    {
        this.repo = repo;
    }

    String SEARCH_WITH_TITLE = "SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
            " FROM movies.person p " +
            " JOIN movies.movie_person mp ON mp.person_id = p.id " +
            " JOIN movies.movie m ON m.id = mp.movie_id";
    String SEARCH_NO_TITLE = "SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
            "FROM movies.person p ";
    @GetMapping("/person/search")
    public ResponseEntity<PersonSearchResponse> foundPersons(PersonSearchRequest request)
    {
        StringBuilder         sql;
        MapSqlParameterSource source     = new MapSqlParameterSource();
        boolean               wereAdded = false;

        sql = new StringBuilder();

        if(request.getMovieTitle() != null)
        {
            sql.append(SEARCH_WITH_TITLE);
            sql.append(" WHERE m.title like :title ");

            String wildcardSearch = '%' + request.getMovieTitle() + '%';

            source.addValue("title", wildcardSearch, Types.VARCHAR);
            wereAdded = true;
        }
        else
        {
            sql.append(SEARCH_NO_TITLE);
        }
        if (request.getName() != null) {
            if (wereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }

            sql.append(" p.name like :name ");
            String wildcardSearch = '%' + request.getName() + '%';
            source.addValue("name", wildcardSearch, Types.VARCHAR);
            wereAdded = true;
        }
        if (request.getBirthday() != null) {
            if (wereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }

            sql.append(" p.birthday = :birthday ");
            source.addValue("birthday", request.getBirthday(), Types.VARCHAR);
            wereAdded = true;
        }
        PersonOrderBy orderBy = PersonOrderBy.fromString(request.getOrderBy());
        sql.append(orderBy.toSql());

        PersonDirection direction = PersonDirection.fromString(request.getDirection());
        sql.append(direction.toSql());

        MovieLimit limit = MovieLimit.fromInt(0);
        if (request.getLimit() != null) {
            limit = MovieLimit.fromInt(request.getLimit()); //throw error if invalid limit
        }
        sql.append(limit.toSql());

        int page = 1;
        if(request.getPage() != null) {
            if(request.getPage() < 1)
                throw new ResultError(MoviesResults.INVALID_PAGE);
            page = request.getPage();
        }
        int offset = ((page - 1) * limit.getValue());
        MovieOffset moff = new MovieOffset(offset);
        sql.append(moff.toSql());

        List<PersonDetail> persons = repo.getTemplate().query(
                sql.toString(),
                source,
                (rs, rowNum) ->
                        new PersonDetail()
                                .setName(rs.getString("p.name"))
                                .setId(rs.getInt("p.id"))
                                .setBiography(rs.getString("p.biography"))
                                .setBirthday(rs.getString("p.birthday"))
                                .setPopularity(rs.getFloat("p.popularity"))
                                .setProfilePath(rs.getString("p.profile_path"))
                                .setBirthplace(rs.getString("p.birthplace"))

        );
        if(persons.isEmpty())
        {
            PersonSearchResponse empty = new PersonSearchResponse()
                    .setPersons(null)
                    .setResult(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH);
            return ResponseEntity.status(empty.getResult().status())
                    .body(empty);
        }
        PersonSearchResponse response = new PersonSearchResponse()
                .setPersons(persons)
                .setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH);
        return ResponseEntity.status(response.getResult().status())
                .body(response);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonGetByPersonIdResponse> foundPersons(@PathVariable("personId") Integer personId)
    {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                " FROM movies.person p "
                );
        MapSqlParameterSource source = new MapSqlParameterSource();
        sql.append("WHERE p.id = :id");
        source.addValue("id",personId, Types.INTEGER);

        PersonGetByPersonIdResponse emptyResponse = new PersonGetByPersonIdResponse()
                .setPerson(null)
                .setResult(MoviesResults.NO_PERSON_WITH_ID_FOUND);
        try {
            PersonDetail person = repo.getTemplate().queryForObject(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new PersonDetail()
                                    .setName(rs.getString("p.name"))
                                    .setId(rs.getInt("p.id"))
                                    .setBiography(rs.getString("p.biography"))
                                    .setBirthday(rs.getString("p.birthday"))
                                    .setPopularity(rs.getFloat("p.popularity"))
                                    .setProfilePath(rs.getString("p.profile_path"))
                                    .setBirthplace(rs.getString("p.birthplace"))
                    );
            PersonGetByPersonIdResponse response = new PersonGetByPersonIdResponse()
                    .setPerson(person)
                    .setResult(MoviesResults.PERSON_WITH_ID_FOUND);
            return ResponseEntity.status(response.getResult().status())
                    .body(response);
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(emptyResponse.getResult().status())
                    .body(emptyResponse);
        }
    }
}
