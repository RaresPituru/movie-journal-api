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
    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("")
    List<Movie> findAll(){
        return movieService.findAll();
    }
    @GetMapping("/{id}")
    Movie findById(@PathVariable Integer id){
        return movieService.findById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Movie movie){
        movieService.create(movie);
    }

    @PutMapping("/{id}")
    void update(@Valid @RequestBody Movie movie,@PathVariable Integer id){
        movieService.update(movie,id);
    }
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        movieService.delete(id);
    }
}
