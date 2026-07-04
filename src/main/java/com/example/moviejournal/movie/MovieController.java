package com.example.moviejournal.movie;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @GetMapping("")
    List<Movie> findAll(){
        return movieRepository.findAll();
    }
    @GetMapping("/{id}")
    Movie findById(@PathVariable Integer id){
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        return movie.get();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Movie movie){
        movieRepository.create(movie);
    }

    @PutMapping("/{id}")
    void update(@Valid @RequestBody Movie movie,@PathVariable Integer id){
        movieRepository.update(movie,id);
    }
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        movieRepository.delete(id);
    }
}
