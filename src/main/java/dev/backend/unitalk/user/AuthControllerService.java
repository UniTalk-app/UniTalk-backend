package dev.backend.unitalk.user;

import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.payload.request.UserRequest;
import dev.backend.unitalk.payload.response.JwtResponse;
import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.role.ERole;
import dev.backend.unitalk.role.Role;
import dev.backend.unitalk.role.RoleRepository;
import dev.backend.unitalk.security.jwt.JwtUtils;
import dev.backend.unitalk.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthControllerService {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    public AuthControllerService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtils jwtUtils) {
        this.authenticationManager=authenticationManager;
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtils=jwtUtils;
    }

    public ResponseEntity<JwtResponse> authenticateUser(UserRequest loginRequest) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<MessageResponse> registerUser(UserRequest registerRequest) throws UserAuthenticationException {

        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername())))
            throw  new UserAuthenticationException("Error: Username is already in use!");

        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerRequest.getEmail())))
            throw new UserAuthenticationException("Error: Email is already in use!");

        // Create new user's account
        var user = new User(
                registerRequest.getUsername(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        var userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
