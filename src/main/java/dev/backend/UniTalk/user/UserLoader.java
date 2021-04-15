package dev.backend.UniTalk.user;

import dev.backend.UniTalk.user.User;
import dev.backend.UniTalk.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userRepository.save(new User("username", "email", "password"));
    }
}
