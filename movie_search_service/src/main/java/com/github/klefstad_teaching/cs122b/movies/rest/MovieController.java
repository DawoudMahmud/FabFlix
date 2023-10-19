package com.github.klefstad_teaching.cs122b.movies.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;
import com.gitcodings.stack.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;
import com.github.klefstad_teaching.cs122b.movies.model.request.MovieGetByMovieIdRequest;
import com.github.klefstad_teaching.cs122b.movies.model.request.MovieSearchByPersonRequest;
import com.github.klefstad_teaching.cs122b.movies.model.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieGetByMovieIdResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchByPersonResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieController
{
    private final MovieRepo repo;
    private final Validate validate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate, ObjectMapper objectMapper)
    {
        this.repo = repo;
        this.validate = validate;
        this.objectMapper = objectMapper;
    }

    private final static String MOVIE_SEARCH = "SELECT DISTINCT m.id, title, year, p.name, rating, backdrop_path, poster_path, hidden, p.id " +
            "FROM movies.movie m " +
            "JOIN movies.person p ON m.director_id = p.id " +
            " JOIN movies.movie_genre mg ON mg.movie_id = m.id " +
            "JOIN movies.genre g ON mg.genre_id = g.id ";


    @GetMapping("/movie/search")
    public ResponseEntity<MovieSearchResponse> foundMovies(@AuthenticationPrincipal SignedJWT user, MovieSearchRequest request) throws ParseException {
        StringBuilder         sql;
        MapSqlParameterSource source     = new MapSqlParameterSource();
        boolean               wereAdded = false;
        boolean               canSeeHidden = false;
        List<String> userRoles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        if(userRoles.contains("ADMIN") || userRoles.contains("EMPLOYEE"))
            canSeeHidden = true;
        sql = new StringBuilder(MOVIE_SEARCH);
        if (request.getDirector() != null) {

            sql.append(" WHERE p.name LIKE :name ");

            String wildcardSearch = '%' + request.getDirector() + '%';

            source.addValue("name", wildcardSearch, Types.VARCHAR);
            wereAdded = true;
        }
        if(request.getTitle() != null) {
            if(wereAdded) {
                sql.append(" AND ");
            }
            else {
                sql.append(" WHERE ");
                wereAdded = true;
            }
            sql.append(" m.title LIKE :title");
            String wildcardSearch = '%' + request.getTitle() + '%';
            source.addValue("title", wildcardSearch, Types.VARCHAR);
        }

        if (request.getGenre() != null) {
            if (wereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                wereAdded = true;
            }

            sql.append(" g.name like :genre ");
            String wildcardSearch2 = '%' + request.getGenre() + '%';
            source.addValue("genre", wildcardSearch2, Types.VARCHAR);
        }

        if (request.getYear() != null) {
            if (wereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }

            sql.append(" m.year = :year ");
            source.addValue("year", request.getYear(), Types.INTEGER);
        }

        MovieOrderBy orderBy = MovieOrderBy.fromString(request.getOrderBy());
        sql.append(orderBy.toSql());

        MovieDirection direction = MovieDirection.fromString(request.getDirection());
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



        List<Movie> movies = repo.getTemplate().query(
                sql.toString(),
                source,
                (rs, rowNum) ->
                        new Movie()
                                .setId(rs.getLong("m.id"))
                                .setTitle(rs.getString("m.title"))
                                .setYear(rs.getInt("year"))
                                .setDirector(repo.getDirectorNameFromId(rs.getInt("p.id")))
                                .setRating(rs.getDouble("rating"))
                                .setBackdropPath(rs.getString("backdrop_path"))
                                .setPosterPath(rs.getString("poster_path"))
                                .setHidden(rs.getBoolean("hidden"))
        );
        if(!canSeeHidden)
            movies.removeIf(Movie::getHidden);
        MovieSearchResponse response = new MovieSearchResponse()
                .setMovies(movies);
        if(movies.isEmpty())
            response.setResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH)
                    .setMovies(null);
        else
            response.setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH);

        return ResponseEntity.status(response.getResult().status())
                .body(response);
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieSearchByPersonResponse> Result(@AuthenticationPrincipal SignedJWT user,  @PathVariable("personId") Long persondId, MovieSearchByPersonRequest request) throws ParseException {
        StringBuilder         sql;
        MapSqlParameterSource source     = new MapSqlParameterSource();
        boolean               wereAdded = false;
        boolean               canSeeHidden = false;
        List<String> userRoles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        if(userRoles.contains("ADMIN") || userRoles.contains("EMPLOYEE"))
            canSeeHidden = true;

        sql = new StringBuilder("SELECT m.title, m.id, m.year, m.director_id, m.rating, m.backdrop_path, m.poster_path, m.hidden " +
                " FROM movies.movie m " +
                " JOIN movies.movie_person mp on mp.movie_id = m.id " +
                " WHERE mp.person_id = :personId");
        source.addValue("personId", persondId, Types.INTEGER);

        MovieOrderBy orderBy = MovieOrderBy.fromString(request.getOrderBy());
        sql.append(orderBy.toSql());

        MovieDirection direction = MovieDirection.fromString(request.getDirection());
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

        List<Movie> movies = repo.getTemplate().query(
                sql.toString(),
                source,
                (rs, rowNum) ->
                        new Movie()
                                .setId(rs.getLong("m.id"))
                                .setTitle(rs.getString("m.title"))
                                .setYear(rs.getInt("m.year"))
                                .setDirector(repo.getDirectorNameFromId(rs.getInt("m.director_id")))
                                .setRating(rs.getDouble("m.rating"))
                                .setBackdropPath(rs.getString("m.backdrop_path"))
                                .setPosterPath(rs.getString("m.poster_path"))
                                .setHidden(rs.getBoolean("m.hidden"))
        );
        if(!canSeeHidden)
            movies.removeIf(Movie::getHidden);
        MovieSearchByPersonResponse response = new MovieSearchByPersonResponse()
                .setMovies(movies);
        if(movies.isEmpty())
            response.setResult(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND)
                    .setMovies(null);
        else
            response.setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND);

        return ResponseEntity.status(response.getResult().status())
                .body(response);

    }




    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieGetByMovieIdResponse> foundMovies (@AuthenticationPrincipal SignedJWT user,  @PathVariable("movieId") Long movieId) throws ParseException {
        boolean               canSeeHidden = false;
        StringBuilder         sql;

        List<String> userRoles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        if(userRoles.contains("ADMIN") || userRoles.contains("EMPLOYEE"))
            canSeeHidden = true;

        MapSqlParameterSource source     = new MapSqlParameterSource();
        sql = new StringBuilder("SELECT m.id, m.title, m.year, m.overview, m.hidden, m.director_id, m.rating, m.backdrop_path, m.poster_path, m.hidden, m.num_votes, m.budget, m.revenue, " +
                " (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', g.id, 'name', g.name)) " +
                "FROM (SELECT DISTINCT g.id, g.name " +
                "FROM movies.genre g " +
                "JOIN movies.movie_genre mg ON g.id = mg.genre_id " );

        sql.append("WHERE mg.movie_id = :movieId " +
                "ORDER BY g.name) as g ) AS genres, " +
                "(SELECT JSON_ARRAYAGG(JSON_OBJECT('id', p.id, 'name', p.name)) " +
                "FROM (SELECT DISTINCT p.id, p.name, p.popularity " +
                "FROM movies.person p " +
                "JOIN movies.movie_person mp ON p.id = mp.person_id " +
                "WHERE mp.movie_id = :movieId " +
                "ORDER BY p.popularity DESC, p.id ASC) as p ) AS persons " +
                "FROM movies.movie m " +
                "WHERE m.id = :movieId ");
        source.addValue("movieId",movieId, Types.INTEGER);

        MovieGetByMovieIdResponse movieInfo = new MovieGetByMovieIdResponse();
        MovieGetByMovieIdResponse emptyResponse = new MovieGetByMovieIdResponse()
                .setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND);
        try {
            movieInfo = repo.getTemplate().queryForObject(
                    sql.toString(),
                    source,
                    this::methodInsteadOfLambdaForMapping);
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(emptyResponse.getResult().status())
                    .body(emptyResponse);
        }


        if(!canSeeHidden) {
            if (movieInfo.getMovie().getHidden())
            {
                return ResponseEntity.status(emptyResponse.getResult().status())
                        .body(emptyResponse);
            }
        }

        return ResponseEntity.status(movieInfo.getResult().status())
                .body(movieInfo);
    }

    private MovieGetByMovieIdResponse methodInsteadOfLambdaForMapping(ResultSet rs, int rowNumber)
            throws SQLException
    {
        List<Genre> genres = null;
        List<Person> persons = null;

        try {
            String jsonArrayStringG = rs.getString("genres");
            String jsonArrayStringP = rs.getString("persons");

            Genre[] genreArray = objectMapper.readValue(jsonArrayStringG, Genre[].class);
            Person[] personArray = objectMapper.readValue(jsonArrayStringP, Person[].class);

            // This just helps convert from an Object Array to a List<>
            genres = Arrays.stream(genreArray).collect(Collectors.toList());
            persons = Arrays.stream(personArray).collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            throw new ResultError(MoviesResults.INVALID_DIRECTION);
        }

        MovieDetail movieInfo =
                new MovieDetail()
                    .setId(rs.getLong("m.id"))
                    .setTitle(rs.getString("m.title"))
                    .setYear(rs.getInt("m.year"))
                    .setDirector(repo.getDirectorNameFromId(rs.getInt("m.director_id")))
                    .setRating(rs.getDouble("m.rating"))
                    .setBackdropPath(rs.getString("m.backdrop_path"))
                    .setPosterPath(rs.getString("m.poster_path"))
                    .setHidden(rs.getBoolean("m.hidden"))
                    .setBudget(rs.getLong("m.budget"))
                    .setNumVotes(rs.getLong("m.num_votes"))
                    .setRevenue(rs.getLong("m.revenue"))
                        .setOverview(rs.getString("m.overview"));

        return new MovieGetByMovieIdResponse()
                .setMovie(movieInfo)
                .setGenres(genres)
                .setPersons(persons)
                .setResult(MoviesResults.MOVIE_WITH_ID_FOUND);
    }

}
