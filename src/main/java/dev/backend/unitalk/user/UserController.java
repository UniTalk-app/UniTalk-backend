package dev.backend.unitalk.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserControllerService userControllerService;

    public UserController(UserControllerService userControllerService) {
        this.userControllerService = userControllerService;
    }

    @GetMapping(path = "/data", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserData(@AuthenticationPrincipal User user) {
        return userControllerService.getUserData(user);
    }

    @GetMapping(path = "/{idGroup}/all")
    public ResponseEntity<Object> getUsersInGroup(@PathVariable Long idGroup, @AuthenticationPrincipal User user) throws Exception {
        return userControllerService.getUsersInGroup(idGroup, user);
    }
}
