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
    public List<Movie> findAll(Integer userId){
        return jdbcClient.sql("select * from movie WHERE user_id = :userId").
                param("userId",userId).query(Movie.class).list();
    }

    public Optional<Movie> findById(Integer id,Integer userId) {
        return jdbcClient.sql("SELECT * FROM movie WHERE id = :id AND user_id = :userId")
                .param("id", id)
                .param("userId",userId)
                .query(Movie.class)
                .optional();
    }

    public void create(Movie movie){
             jdbcClient.sql("INSERT INTO movie (id,title,rating,review,release_year,user_id) " +
                             "VALUES(:id, :title, :rating, :review, :release_year, :user_id)")
               .param("id",movie.id())
               .param("title",movie.title())
               .param("rating",movie.rating())
               .param("review",movie.review())
               .param("release_year",movie.releaseYear()).param("user_id",movie.userId()).update();
    }
    public void update(Movie movie,Integer id,Integer userId){
        jdbcClient.sql("UPDATE movie SET title = :title, rating = :rating, review = :review," +
                        "release_year = :release_year WHERE id = :id AND user_id = :userId")
                .param("title", movie.title())
                .param("rating",movie.rating())
                .param("review",movie.review())
                .param("release_year",movie.releaseYear())
                .param("id",id)
                .param("userId",userId).update();
    }

    public void delete(Integer id,Integer userId){
        jdbcClient.sql("delete from movie where id = :id AND user_id = :userId")
                .param("id",id)
                .param("userId",userId)
                .update();
    }
}
