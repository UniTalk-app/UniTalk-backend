package dev.backend.UniTalk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("username", "email", encoder.encode("qwerty"));
        this.userRepository.save(user);
    }
}
