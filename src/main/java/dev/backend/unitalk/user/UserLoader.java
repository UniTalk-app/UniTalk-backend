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
        var user = new User("admin", "admin", "admin", "email@email", encoder.encode("qwerty#"));
        var roles = user.getRoles();
        var userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
        var modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow();
        var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();

        roles.add(userRole);
        roles.add(modRole);
        roles.add(adminRole);
        user.setRoles(roles);
        this.userRepository.save(user);
    }
}