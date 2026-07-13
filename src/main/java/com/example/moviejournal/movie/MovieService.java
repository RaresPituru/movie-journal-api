package com.example.moviejournal.movie;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll(Integer userId){
        return movieRepository.findAll(userId);
    }
   public void create(Movie movie,Integer userId){
        Movie movieWithUser = new Movie(movie.id(),movie.title(),movie.rating(),
                movie.review(),movie.releaseYear(),userId);

        movieRepository.create(movieWithUser);
    }
    public void update(Movie movie,Integer id,Integer userId){
        movieRepository.update(movie,id,userId);
    }
    public void delete(Integer id,Integer userId){
        movieRepository.delete(id,userId);
    }
   public Movie findById(Integer id,Integer userId){
        Optional<Movie> movie = movieRepository.findById(id,userId);
        if(movie.isEmpty()){
            throw new MovieNotFoundException("Movie not found with id " + id);
        }
        return movie.get();
    }
}
