package dev.backend.UniTalk.user;

import dev.backend.UniTalk.role.ERole;
import dev.backend.UniTalk.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("username", "firstName", "lastName", "email@email", encoder.encode("qwerty"));
        var roles = user.getRoles();
        var role = roleRepository.findByName(ERole.ROLE_USER);
        if (role.isEmpty()) {
            return;
        }
        roles.add(role.get());
        user.setRoles(roles);
        this.userRepository.save(user);
    }
}