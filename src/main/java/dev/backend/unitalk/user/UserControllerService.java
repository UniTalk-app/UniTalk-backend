package dev.backend.unitalk.user;


import dev.backend.unitalk.role.Role;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;



@Service
public class UserControllerService {

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

}






