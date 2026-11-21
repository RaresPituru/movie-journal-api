package com.example.moviejournal.movie;

import com.example.moviejournal.AbstractIntegrationTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieIntegrationTest extends AbstractIntegrationTest {

    @Test
    void requestWithoutToken_isRejectedWith401() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/movies"));
    }

    @Test
    void requestWithInvalidToken_isRejectedWith401() throws Exception {
        mockMvc.perform(get("/api/movies").header(HttpHeaders.AUTHORIZATION, bearer("not.a.token")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createThenFetchOwnMovie() throws Exception {
        String token = newUserToken();

        int id = createMovie(token, "Inception", 9, 2010);

        mockMvc.perform(get("/api/movies/{id}", id).header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.rating").value(9))
                .andExpect(jsonPath("$.releaseYear").value(2010))
                .andExpect(jsonPath("$.userId").doesNotExist());
    }

    @Test
    void userB_cannotAccessUserAsMovie() throws Exception {
        String tokenA = newUserToken();
        String tokenB = newUserToken();
        int movieOfA = createMovie(tokenA, "Alien", 8, 1979);

        mockMvc.perform(get("/api/movies/{id}", movieOfA).header(HttpHeaders.AUTHORIZATION, bearer(tokenB)))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/api/movies/{id}", movieOfA)
                        .header(HttpHeaders.AUTHORIZATION, bearer(tokenB))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson("Hacked", 1, 1979)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/movies/{id}", movieOfA).header(HttpHeaders.AUTHORIZATION, bearer(tokenB)))
                .andExpect(status().isNotFound());

        // A's movie is untouched.
        mockMvc.perform(get("/api/movies/{id}", movieOfA).header(HttpHeaders.AUTHORIZATION, bearer(tokenA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Alien"));
    }

    @Test
    void invalidBody_returns400WithFieldErrors() throws Exception {
        String token = newUserToken();

        mockMvc.perform(post("/api/movies")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": " ", "rating": 11, "releaseYear": 1500}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors[*].field",
                        containsInAnyOrder("title", "rating", "releaseYear")));
    }

    @Test
    void pagination_sortingAndFiltering_returnCorrectValues() throws Exception {
        String token = newUserToken();
        for (int rating = 1; rating <= 7; rating++) {
            createMovie(token, "Movie " + rating, rating, 2000 + rating);
        }

        // 7 movies, 3 per page -> 3 pages. Sorted by rating desc: [7,6,5] [4,3,2] [1],
        // so page 1 (zero-based) contains ratings 4, 3, 2.
        mockMvc.perform(get("/api/movies")
                        .param("page", "1").param("size", "3")
                        .param("sort", "rating").param("direction", "desc")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].rating", contains(4, 3, 2)))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.totalPages").value(3));

        mockMvc.perform(get("/api/movies")
                        .param("minRating", "6").param("sort", "releaseYear")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].title", contains("Movie 6", "Movie 7")))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void pagination_rejectsInvalidParameters() throws Exception {
        String token = newUserToken();

        mockMvc.perform(get("/api/movies")
                        .param("size", "51").param("sort", "password")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("size", "sort")));
    }

    @Test
    void stats_returnCorrectValues() throws Exception {
        String token = newUserToken();
        createMovie(token, "Alien", 8, 1979);
        createMovie(token, "Heat", 9, 1995);
        createMovie(token, "Up", 7, 2009);

        mockMvc.perform(get("/api/movies/stats").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMovies").value(3))
                .andExpect(jsonPath("$.averageRating").value(8.0))
                .andExpect(jsonPath("$.highestRatedMovie.title").value("Heat"))
                .andExpect(jsonPath("$.highestRatedMovie.rating").value(9));
    }

    @Test
    void stats_forUserWithoutMovies_returnsZeroAndNulls() throws Exception {
        String token = newUserToken();

        mockMvc.perform(get("/api/movies/stats").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMovies").value(0))
                .andExpect(jsonPath("$.averageRating").isEmpty())
                .andExpect(jsonPath("$.highestRatedMovie").isEmpty());
    }

    private int createMovie(String token, String title, int rating, int releaseYear) throws Exception {
        String body = mockMvc.perform(post("/api/movies")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson(title, rating, releaseYear)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private static String movieJson(String title, int rating, int releaseYear) {
        return """
                {"title": "%s", "rating": %d, "review": "test review", "releaseYear": %d}
                """.formatted(title, rating, releaseYear);
    }
}
