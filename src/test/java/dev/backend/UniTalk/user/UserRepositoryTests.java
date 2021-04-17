package dev.backend.UniTalk.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void UserRepositoryCountReturnsCorrectly() {
        entityManager.clear();
        long userCount = userRepository.count();
        Assertions.assertEquals(0, userCount);
    }

    @Test
    void UserRepositorySavesUser() {
        entityManager.clear();
        User user = new User("Username", "email", "password");
        entityManager.persistAndFlush(user);
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(user, userRepository.findById(user.getId()).get());
    }

    @Test
    void UserRepositoryFindsByAndExistsTests() {
        entityManager.clear();
        User user = new User("Username", "email", "password");
        entityManager.persistAndFlush(user);
        Assertions.assertEquals(user, userRepository.findByUsername(user.getUsername()).get());
        Assertions.assertTrue(userRepository.existsByEmail("email"));
        Assertions.assertTrue(userRepository.existsByUsername("Username"));
        Assertions.assertFalse(userRepository.existsByEmail("eemail"));
        Assertions.assertFalse(userRepository.existsByUsername("UUsername"));
    }
}
