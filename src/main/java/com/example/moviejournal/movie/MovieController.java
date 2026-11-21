package com.example.moviejournal.movie;

import com.example.moviejournal.security.AuthenticatedUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Example: GET /api/movies?page=0&size=10&sort=rating&direction=desc&minRating=7
     * Invalid values are rejected with 400 (Spring validates the constrained parameters).
     */
    @GetMapping
    PageResponse<MovieResponse> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "title") @Pattern(regexp = "title|rating|releaseYear") String sort,
            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE) String direction,
            @RequestParam(required = false) @Min(1) @Max(10) Integer minRating,
            @AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.findAll(user.id(), page, size, sort, direction, minRating);
    }

    // A literal path like /stats takes precedence over the /{id} pattern.
    @GetMapping("/stats")
    MovieStatsResponse stats(@AuthenticationPrincipal AuthenticatedUser user) {
        return movieService.getStats(user.id());
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
