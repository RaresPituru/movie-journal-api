package com.example.moviejournal.movie;

import com.example.moviejournal.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final UserRepository userRepository;

    public MovieController(MovieService movieService,UserRepository userRepository){
        this.movieService = movieService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    List<Movie> findAll(){
        return movieService.findAll(getCurrentUserId());
    }

    @GetMapping("/{id}")
    Movie findById(@PathVariable Integer id){
        return movieService.findById(id,getCurrentUserId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Movie movie){
        movieService.create(movie,getCurrentUserId());
    }

    @PutMapping("/{id}")
    void update(@Valid @RequestBody Movie movie,@PathVariable Integer id){
        movieService.update(movie,id,getCurrentUserId());
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        movieService.delete(id,getCurrentUserId());
    }

    private Integer getCurrentUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}
