import React from "react";
import styled from "styled-components";
import {Dropdown} from "react-bootstrap";
import Axios from "axios"
import {useForm} from "react-hook-form";
import {useUser} from "../hook/User";
import {search} from "../backend/movies";
import login from "./Login";
import {useSearchParams} from "react-router-dom";

const StyledDiv = styled.div` 
`

const StyledH1 = styled.h1`
`






const StyledTable = styled.table`
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
`
const StyledTD = styled.td`
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
`

const StyledTH = styled.th`
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
`

const MovieSearch = () => {


    const [movies, setMovies] = React.useState([]);

    const[pageNumber, setPageNumber] = React.useState(1);
    const[searchParams, setSearchParams] = useSearchParams();


    const increasePage = ()  => {
        setPageNumber(pageNumber + 1);
        getMovies(pageNumber+1)
    }
    const decreasePage = ()  => {
        setPageNumber(pageNumber - 1);
        getMovies(pageNumber-1)
    }
    const firstPage = () => {
        getMovies(1)
    }
    const {register, getValues, handleSubmit} = useForm(
        {
            defaultValues: {
                title: searchParams.get("title"),
                director: searchParams.get("director"),
                year: searchParams.get("year"),
                genre: searchParams.get("genre")
            }
        }
    );

    const {accessToken} = useUser();


    const getMovies = (pageNumber) => {
        const title = getValues("title");
        const director = getValues("director")
        const year = getValues("year")
        const rating = getValues("rating")
        const backdrop = getValues("backdrop_path")
        const poster = getValues("poster_path")
        const genre = getValues("genre")
        const direction = getValues("direction")
        const orderBy = getValues("orderBy")
        const page = pageNumber
        const limit = getValues("limit")


        const queryParams = {
            title: title !== "" ? title : undefined,
            director: director !== "" ? director : undefined,
            year: year !== "" ? year : undefined,
            rating: rating !== "" ? rating : undefined,
            backdrop: backdrop !== "" ? backdrop : undefined,
            poster: poster !== "" ? poster : undefined,
            genre: genre !== "" ? genre : undefined,
            direction: direction !== "" ? direction : "ASC",
            orderBy: orderBy !== "" ? orderBy : "title",
            limit: limit !== "" ? limit : 10,
            page: page
        };
        setSearchParams(queryParams);


        search(queryParams, accessToken)
            .then(response => setMovies(response.data.movies))
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
    }

    return (
        <div>


            Title: <input {...register("title")}/>
            <br></br>
            Director: <input {...register("director")}/>
            <br></br>
            Year: <input {...register("year")}/>
            <br></br>
            Genre: <input {...register("genre")}/>
            <br></br>

            Order By:
            <select placeholder={"title"} {...register("orderBy")}>
                <option value={"title"}>Title</option>
                <option value={"rating"}>Rating</option>
                <option value={"year"}>Year</option>
            </select>
            <br></br>
            Direction:
            <select placeholder={""} {...register("direction")}>
                <option value={"ASC"}>Ascending</option>
                <option value={"DESC"}>Descending</option>
            </select>
            <br></br>
            Display:
            <select placeholder={""} {...register("limit")}>
                <option value={10}>10</option>
                <option value={25}>25</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
            </select>
            <br></br>
            <br></br>
            <br></br>
            <button onClick={handleSubmit(firstPage)}>Get Movies!</button>

            <br></br>
            <br></br>
            <StyledTable>
                <tr>
                    <StyledTH>Title</StyledTH>
                    <StyledTH>Director</StyledTH>
                    <StyledTH>Rating</StyledTH>
                    <StyledTH>Year</StyledTH>
                </tr>
                {movies && movies.map(movie =>
                    <tr>
                        <StyledTD> {movie.title} </StyledTD>
                        <StyledTD>{movie.director}</StyledTD>
                        <StyledTD>{movie.rating}</StyledTD>
                        <StyledTD> {movie.year} </StyledTD>
                    </tr>)}
            </StyledTable>
            <br></br>
            <br></br>
            <button onClick={handleSubmit(decreasePage)}>Previous</button>
            <button onClick={handleSubmit(increasePage)}>Next</button>








        </div>
    );
}

export default MovieSearch;



{/*{movies.map(movie => <p> {movie.director} </p>)}*/}
{/*<input {...register("director")}/>*/}

{/*{movies.map(movie => <p> {movie.year} </p>)}*/}
{/*<input {...register("year")}/>*/}

{/*{movies.map(movie => <p> {movie.genre} </p>)}*/}
{/*<input {...register("genre")}/>*/}

{/*{movies.map(movie => <p> {movie.direction} </p>)}*/}
{/*<input {...register("direction")}/>*/}

{/*{movies.map(movie => <p> {movie.orderBy} </p>)}*/}
{/*<input {...register("orderBy")}/>*/}

{/*{movies.map(movie => <p> {movie.limit} </p>)}*/}
{/*<input {...register("limit")}/>*/}

{/*{movies.map(movie => <p> {movie.page} </p>)}*/}
{/*<input {...register("page")}/>*/}
