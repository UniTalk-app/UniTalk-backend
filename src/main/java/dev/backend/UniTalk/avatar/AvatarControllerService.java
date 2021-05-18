package dev.backend.UniTalk.avatar;



import dev.backend.UniTalk.exception.ResourceNotFoundException;
import dev.backend.UniTalk.user.User;
import dev.backend.UniTalk.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarControllerService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarControllerService(AvatarRepository avatarRepository,
                                   UserRepository userRepository) {
        this.avatarRepository=avatarRepository;
        this.userRepository=userRepository;
    }




    public byte[] getAvatar(User user) {

//        final Avatar avatarFromBD = avatarRepository.findBy(user.getAvatarId())
//                .orElseThrow(() -> new ResourceNotFoundException("Not found avatar with id = " + user.getAvatarId()));

        //Avatar avatar = user.getAvatar();
        if(user.getAvatar() == null)
            throw new ResourceNotFoundException("User does not have an avatar");

        return user.getAvatar().getImage();
    }

    public ResponseEntity<HttpStatus> addAvatar(User user, MultipartFile imageFile) throws IOException {

        if(user.getAvatar() != null)
            throw new IOException("User already has an avatar");

        Avatar avatar = new Avatar(imageFile.getBytes());

        user.setAvatar(avatar);
        avatar.setUser(user);

//        avatarRepository.save(avatar);
//        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> updateAvatar(User user, MultipartFile imageFile) throws IOException {

//        Avatar avatar = avatarRepository.findById(user.getAvatarId())
//                .orElseThrow(() -> new ResourceNotFoundException("Not found avatar with id = " + user.getAvatarId()));

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException("User does not have an avatar");

        Avatar avatar = user.getAvatar();

        avatar.setImage(imageFile.getBytes());
        avatarRepository.save(avatar);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteOne(User user) {

        if(user.getAvatar() == null)
            throw new ResourceNotFoundException("User does not have an avatar");

        avatarRepository.delete(user.getAvatar());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
