import Config from "backend/config.json";
import Axios from "axios";


/**
 * We use axios to create REST calls to our backend
 *
 * We have provided the login rest call for your
 * reference to build other rest calls with.
 *
 * This is an async function. Which means calling this function requires that
 * you "chain" it with a .then() function call.
 * <br>
 * What this means is when the function is called it will essentially do it "in
 * another thread" and when the action is done being executed it will do
 * whatever the logic in your ".then()" function you chained to it
 * @example
 * login(request)
 * .then(response => alert(JSON.stringify(response.data, null, 2)));
 */
export async function search(SearchRequest, accessToken) {
    const requestBody = {
       title: SearchRequest.title,
        director: SearchRequest.director,
        year: SearchRequest.year,
        genre: SearchRequest.genre,
        direction: SearchRequest.direction,
        orderBy: SearchRequest.orderBy,
        page: SearchRequest.page,
        limit: SearchRequest.limit

    };

    const options = {
        method: "GET",
        baseURL: Config.searchUrl,
        url: Config.movies.search,
        params: requestBody,
        headers: {
            Authorization: "Bearer " + accessToken
        }
    }

    return Axios.request(options);
}


export default {
   search
}