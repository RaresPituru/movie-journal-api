package com.example.moviejournal.movie;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll(Integer userId) {
        return movieRepository.findAll(userId);
    }

    public Movie findById(Integer id, Integer userId) {
        return movieRepository.findById(id, userId)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public Movie create(Movie movie, Integer userId) {
        // Ignore any id/userId coming from the client: the database generates the id
        // and the owner is always the authenticated user.
        Movie toSave = new Movie(null, movie.title(), movie.rating(), movie.review(), movie.releaseYear(), userId);
        return movieRepository.create(toSave);
    }

    public Movie update(Integer id, Movie movie, Integer userId) {
        return movieRepository.update(id, movie, userId)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public void delete(Integer id, Integer userId) {
        if (movieRepository.delete(id, userId) == 0) {
            throw new MovieNotFoundException(id);
        }
    }
}
