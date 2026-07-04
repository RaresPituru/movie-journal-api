package com.example.moviejournal.movie;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository {
    private final JdbcClient jdbcClient;

    public MovieRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }
    public List<Movie> findAll(){
        return jdbcClient.sql("select * from movie").query(Movie.class).list();
    }
    public Optional<Movie> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM movie WHERE id = :id")
                .param("id", id)
                .query(Movie.class)
                .optional();
    }
    public void create(Movie movie){
             jdbcClient.sql("INSERT INTO movie (id,title,rating,review,release_year) VALUES(:id, :title, :rating, :review, :release_year)")
               .param("id",movie.id())
               .param("title",movie.title())
               .param("rating",movie.rating())
               .param("review",movie.review())
               .param("release_year",movie.releaseYear()).update();
    }
    public void update(Movie movie,Integer id){
        jdbcClient.sql("UPDATE movie SET title = :title, rating = :rating, review = :review, release_year = :release_year WHERE id = :id")
                .param("title", movie.title())
                .param("rating",movie.rating())
                .param("review",movie.review())
                .param("release_year",movie.releaseYear())
                .param("id",id).update();
    }

    public void delete(Integer id){
        jdbcClient.sql("delete from movie where id = :id")
                .param("id",id)
                .update();
    }
}
