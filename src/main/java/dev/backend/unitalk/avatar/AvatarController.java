package dev.backend.unitalk.avatar;

import dev.backend.unitalk.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarControllerService avatarControllerService;

    public AvatarController(AvatarControllerService avatarControllerService) {
        this.avatarControllerService=avatarControllerService;
    }


    @ResponseBody
    @GetMapping(value = "", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAvatar(@AuthenticationPrincipal User user) {
        return avatarControllerService.getAvatar(user);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> addAvatar(@RequestParam("image") final MultipartFile imageFile,
                                                @AuthenticationPrincipal User user) throws IOException {
        return avatarControllerService.addAvatar(user, imageFile);
    }

    @PutMapping("")
    public ResponseEntity<HttpStatus> updateAvatar(@RequestParam("image") final MultipartFile imageFile,
                                                   @AuthenticationPrincipal User user) throws IOException {
        return avatarControllerService.updateAvatar(user, imageFile);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteOne(@AuthenticationPrincipal User user) {
        return avatarControllerService.deleteOne(user);
    }
}
