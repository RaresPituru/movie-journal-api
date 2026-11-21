package com.example.moviejournal.movie;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Maps between DTOs (MovieRequest/MovieResponse) and the internal Movie model,
 * so controllers stay thin and the repository only knows about Movie.
 */
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public PageResponse<MovieResponse> findAll(Integer userId, int page, int size, String sort,
                                               String direction, Integer minRating) {
        long offset = (long) page * size; // long: page * size could overflow an int
        List<MovieResponse> content = movieRepository.findPage(userId, minRating, sort, direction, size, offset)
                .stream()
                .map(MovieResponse::from)
                .toList();
        long totalElements = movieRepository.count(userId, minRating);
        return PageResponse.of(content, page, size, totalElements);
    }

    public MovieStatsResponse getStats(Integer userId) {
        return movieRepository.getStats(userId);
    }

    public MovieResponse findById(Integer id, Integer userId) {
        return movieRepository.findById(id, userId)
                .map(MovieResponse::from)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public MovieResponse create(MovieRequest request, Integer userId) {
        // id is null: the database generates it. The owner is always the authenticated user.
        Movie movie = toMovie(null, request, userId);
        return MovieResponse.from(movieRepository.create(movie));
    }

    public MovieResponse update(Integer id, MovieRequest request, Integer userId) {
        return movieRepository.update(id, toMovie(id, request, userId), userId)
                .map(MovieResponse::from)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public void delete(Integer id, Integer userId) {
        if (movieRepository.delete(id, userId) == 0) {
            throw new MovieNotFoundException(id);
        }
    }

    private static Movie toMovie(Integer id, MovieRequest request, Integer userId) {
        return new Movie(id, request.title(), request.rating(), request.review(), request.releaseYear(), userId);
    }
}
