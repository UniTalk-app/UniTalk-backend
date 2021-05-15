package dev.backend.UniTalk.group;

import dev.backend.UniTalk.exception.ResourceNotFoundException;
<<<<<<< Updated upstream
import dev.backend.UniTalk.security.services.UserDetailsImpl;
import org.springframework.hateoas.EntityModel;
=======
import dev.backend.UniTalk.payload.response.MessageResponse;
import dev.backend.UniTalk.user.User;
import dev.backend.UniTalk.user.UserRepository;
>>>>>>> Stashed changes
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GroupControllerService {

    private final GroupRepository repository;

    public GroupControllerService(GroupRepository repository) {
        this.repository = repository;
    }

<<<<<<< Updated upstream
    public List<EntityModel<Group>> all() {

        return repository.findAll().stream()
                .map(group -> EntityModel.of(group,
                        linkTo(methodOn(GroupController.class).one(group.getGroupId())).withSelfRel(),
                        linkTo(methodOn(GroupController.class).all()).withRel("groups")))
                .collect(Collectors.toList());
=======
    public List<Group> all(User user) {
        return new ArrayList<>(user.getGroups());
>>>>>>> Stashed changes
    }

    public List<Group> userGroups(UserDetailsImpl userDetails){

        Long userId = userDetails.getId();
        return repository.findByCreatorId(userId);
    }

    public Group one(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));
    }

    public Group newGroup(Group newGroup) {

        Group group = new Group(newGroup.getGroupName(), newGroup.getCreatorId(),
                newGroup.getCreationTimestamp());

        return repository.save(group);
    }

    public Group replaceGroup(Group newGroup, Long id) {

        return repository.findById(id)
                .map(group -> {
                    group.setGroupName(newGroup.getGroupName());
                    group.setCreatorId(newGroup.getCreatorId());
                    group.setCreationTimestamp(newGroup.getCreationTimestamp());
                    return repository.save(group);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));

        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
<<<<<<< Updated upstream
=======

    public ResponseEntity<MessageResponse> joinLeaveGroup(Long id, User user, int method) {
        var targetGroup = groupRepository.findById(id);
        if (targetGroup.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Invalid request!"));
        }

        var groups = user.getGroups();
        if (
                method == 0 && groups.contains(targetGroup.get()) ||
                method != 0 && !groups.contains(targetGroup.get())
        ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Invalid request!"));
        }

        if (method == 0) {
            groups.add(targetGroup.get());
        }
        else {
            groups.remove(targetGroup.get());
        }
        user.setGroups(groups);

        userRepository.save(user);
        var methodName = method == 0 ? "joined" : "left";
        return ResponseEntity.ok(new MessageResponse("Successfully " + methodName));
    }
>>>>>>> Stashed changes
}
