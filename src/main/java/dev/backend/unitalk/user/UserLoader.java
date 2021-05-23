package dev.backend.unitalk.user;

import dev.backend.unitalk.role.ERole;
import dev.backend.unitalk.role.RoleRepository;
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
        var user = new User("username", "firstName", "lastName", "email@email", encoder.encode("qwerty"));
        var roles = user.getRoles();
        var role = roleRepository.findByName(ERole.ROLE_USER);
        if (role.isEmpty()) {
            return;
        }
        roles.add(role.get());
        user.setRoles(roles);
        this.userRepository.save(user);

        var user2 = new User("username2", "firstName", "lastName", "email2@email", encoder.encode("qwerty"));
        var roles2 = user2.getRoles();
        var role2 = roleRepository.findByName(ERole.ROLE_USER);
        if (role2.isEmpty()) {
            return;
        }
        roles2.add(role2.get());
        user2.setRoles(roles2);
        this.userRepository.save(user2);
    }
}