package com.example.moviejournal.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public void create(User user){
        jdbcClient.sql("INSERT INTO users (id,username,password,role) VALUES (:id, :username, :password, :role)")
                .param("id",user.getId())
                .param("username",user.getUsername())
                .param("password",user.getPassword())
                .param("role",user.getRole()).update();
    }
    public Optional<User> findByUsername(String name){
        return jdbcClient.sql("SELECT * FROM users WHERE username = :name").param("name",name)
                .query(User.class)
                .optional();
    }
}
