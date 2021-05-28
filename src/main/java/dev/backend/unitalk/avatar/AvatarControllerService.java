package dev.backend.unitalk.avatar;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.user.User;
import dev.backend.unitalk.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarControllerService {
    private static final String NOT_HAVE_AVATAR = "User does not have an avatar";
    private static final String NOT_FOUND_USER = "Not found user with username = ";
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarControllerService(AvatarRepository avatarRepository,
                                   UserRepository userRepository) {
        this.avatarRepository=avatarRepository;
        this.userRepository=userRepository;
    }


    public byte[] getAvatar(User user) {

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException(NOT_HAVE_AVATAR);

        return user.getAvatar().getImage();
    }

    public byte[] getAvatarByUsername(String username) {

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER + username));

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException(NOT_HAVE_AVATAR);

        return user.getAvatar().getImage();
    }

    public ResponseEntity<HttpStatus> addAvatar(User user, MultipartFile imageFile) throws IOException {

        if(user.getAvatar() != null)
            throw new IOException("User already has an avatar");

        var avatar = new Avatar(imageFile.getBytes());
        user.setAvatar(avatar);
        avatar.setUser(user);
        avatarRepository.save(avatar);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> updateAvatar(User user, MultipartFile imageFile) throws IOException {

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException(NOT_HAVE_AVATAR);

        var avatar = user.getAvatar();
        avatar.setImage(imageFile.getBytes());
        avatarRepository.save(avatar);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteOne(User user) {

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException(NOT_HAVE_AVATAR);

        avatarRepository.delete(user.getAvatar());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
