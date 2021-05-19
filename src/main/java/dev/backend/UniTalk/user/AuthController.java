package dev.backend.UniTalk.user;

import dev.backend.UniTalk.exception.UserAuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthControllerService authControllerService;

    public AuthController(AuthControllerService authControllerService) {
        this.authControllerService=authControllerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody User loginRequest) {

        return authControllerService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User registerRequest) throws UserAuthenticationException {

        return authControllerService.registerUser(registerRequest);
    }
}