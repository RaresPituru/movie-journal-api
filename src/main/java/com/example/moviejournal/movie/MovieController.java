package com.example.moviejournal.movie;

import com.example.moviejournal.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    List<MovieResponse> findAll(@AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.findAll(user.id());
    }

    @GetMapping("/{id}")
    MovieResponse findById(@PathVariable Integer id, @AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    MovieResponse create(@Valid @RequestBody MovieRequest request, @AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.create(request, user.id());
    }

    @PutMapping("/{id}")
    MovieResponse update(@PathVariable Integer id, @Valid @RequestBody MovieRequest request,
                         @AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.update(id, request, user.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Integer id, @AuthenticationPrincipal AuthenticatedUser user) {
        movieService.delete(id, user.id());
    }
}
