package com.example.moviejournal.movie;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }
   public void create(Movie movie){
        movieRepository.create(movie);
    }
    public void update(Movie movie,Integer id){
        movieRepository.update(movie,id);
    }
    public void delete(Integer id){
        movieRepository.delete(id);
    }
   public Movie findById(Integer id){
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new MovieNotFoundException("Movie not found with id " + id);
        }
        return movie.get();
    }
}
