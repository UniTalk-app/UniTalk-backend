package dev.backend.unitalk.avatar;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarControllerService {
    private static final String NOT_HAVE_AVATAR = "User does not have an avatar";
    private final AvatarRepository avatarRepository;

    public AvatarControllerService(AvatarRepository avatarRepository) {
        this.avatarRepository=avatarRepository;
    }


    public byte[] getAvatar(User user) {

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
