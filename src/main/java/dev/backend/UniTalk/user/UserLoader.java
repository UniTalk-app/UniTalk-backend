package dev.backend.UniTalk.user;

import dev.backend.UniTalk.role.ERole;
import dev.backend.UniTalk.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserLoader(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("username", "firstName", "lastName", "email@email", encoder.encode("qwerty"));
        var roles = user.getRoles();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
        user.setRoles(roles);
        this.userRepository.save(user);
    }
}
