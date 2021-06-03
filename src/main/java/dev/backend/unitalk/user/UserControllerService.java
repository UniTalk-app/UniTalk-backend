package dev.backend.unitalk.user;


import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.group.GroupRepository;
import dev.backend.unitalk.role.Role;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class UserControllerService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public UserControllerService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> getUserData(User user) {

        Role role = user.getRoles().stream().findFirst().orElse(null);

        if(role == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<JSONObject> entities = new ArrayList<>();
        JSONObject entity = new JSONObject();
        entity.put("username", user.getUsername());
        entity.put("role", role.getName());
        entities.add(entity);

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    public ResponseEntity<Object> getUsersInGroup(Long idGroup, User user) throws Exception {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if (!user.getGroups().contains(group)) {
            throw new UserAuthenticationException("No access");
        }

        var users = new ArrayList<JSONObject>();
        group.getUsers().forEach(u -> {
            var entity = new JSONObject();
            entity.put("id", u.getId());
            entity.put("username", u.getUsername());
            users.add(entity);
        });

        return ResponseEntity.ok().body(users);
    }
}






