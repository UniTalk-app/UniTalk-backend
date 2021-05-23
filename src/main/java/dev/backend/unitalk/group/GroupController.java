package dev.backend.unitalk.group;

import java.util.List;

import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.hateoas.EntityModel;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupControllerService groupControllerService;

    public GroupController(GroupControllerService groupControllerService) {
        this.groupControllerService=groupControllerService;
    }

    @GetMapping("/all")
    public List<Group> all(@AuthenticationPrincipal User user) {
        return groupControllerService.all(user);
    }

    @GetMapping("/{id}")
    public EntityModel<Group> one(@PathVariable Long id) {

        var group = groupControllerService.one(id);
        return EntityModel.of(group);
    }

    @PostMapping("/")
    public Group newGroup(@Valid @RequestBody Group newGroup) {
        return groupControllerService.newGroup(newGroup);
    }


    @PutMapping("/{id}")
    public Group replaceGroup(@Valid @RequestBody Group newGroup, @PathVariable Long id) {
        return groupControllerService.replaceGroup(newGroup, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id){
        return groupControllerService.deleteOne(id);
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAll() {
        return groupControllerService.deleteAll();
    }

    @GetMapping("/join/{id}")
    public ResponseEntity<MessageResponse> joinGroup(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return groupControllerService.joinLeaveGroup(id, user, 0);
    }

    @GetMapping("/leave/{id}")
    public ResponseEntity<MessageResponse> leaveGroup(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return groupControllerService.joinLeaveGroup(id, user, 1);
    }
}