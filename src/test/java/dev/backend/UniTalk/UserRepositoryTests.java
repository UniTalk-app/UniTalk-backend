package dev.backend.UniTalk;

import dev.backend.UniTalk.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void UserRepositoryCountReturnsCorrectly() {
        long userCount = userRepository.count();
        Assertions.assertEquals(1, userCount);
    }
}
