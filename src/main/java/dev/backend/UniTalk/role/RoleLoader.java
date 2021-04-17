package dev.backend.UniTalk.role;

import dev.backend.UniTalk.role.ERole;
import dev.backend.UniTalk.role.Role;
import dev.backend.UniTalk.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleRepository.save(new Role(ERole.ROLE_USER));
        this.roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        this.roleRepository.save(new Role(ERole.ROLE_ADMIN));
    }
}
