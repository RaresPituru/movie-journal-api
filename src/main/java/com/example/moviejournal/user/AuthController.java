package com.example.moviejournal.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody User user){
        userService.register(user);
        return "User registered successfully";
    }
    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.login(user.getUsername(), user.getPassword());
    }
}