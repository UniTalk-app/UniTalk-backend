package dev.backend.unitalk.user;

import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.payload.request.UserRequest;
import dev.backend.unitalk.payload.response.JwtResponse;
import dev.backend.unitalk.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserRequest loginRequest) {

        return authControllerService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody UserRequest registerRequest) throws UserAuthenticationException {

        return authControllerService.registerUser(registerRequest);
    }

    //TODO: Check token validity
/*    @PostMapping("/check")
    public ResponseEntity<HttpStatus> checkToken()*/
}