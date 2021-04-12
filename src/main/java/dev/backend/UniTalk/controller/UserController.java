package dev.backend.UniTalk.controller;

import dev.backend.UniTalk.model.User;
import dev.backend.UniTalk.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository repository;

    public UserController(UserRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(value = "/all")
    public  List<User> getAllUsers()
    {
        return repository.findAll();
    }
}
