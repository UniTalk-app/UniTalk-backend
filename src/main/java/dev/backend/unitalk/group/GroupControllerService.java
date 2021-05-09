package dev.backend.unitalk.group;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.security.services.UserDetailsImpl;
import dev.backend.unitalk.user.UserRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class GroupControllerService {

    private static final String NOT_FOUND_GROUP = "Not found group with id = ";
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupControllerService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<EntityModel<Group>> all() {

        return groupRepository.findAll().stream()
                .map(group -> EntityModel.of(group,
                        linkTo(methodOn(GroupController.class).one(group.getGroupId())).withSelfRel(),
                        linkTo(methodOn(GroupController.class).all()).withRel("groups")))
                .collect(Collectors.toList());
    }

    public Group one(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + id));
    }

    public Group newGroup(Group newGroup) {

        var group = new Group(newGroup.getGroupName(), newGroup.getCreatorId(),
                newGroup.getCreationTimestamp());

        return groupRepository.save(group);
    }

    public Group replaceGroup(Group newGroup, Long id) {

        return groupRepository.findById(id)
                .map(group -> {
                    group.setGroupName(newGroup.getGroupName());
                    group.setCreatorId(newGroup.getCreatorId());
                    group.setCreationTimestamp(newGroup.getCreationTimestamp());
                    return groupRepository.save(group);
                })
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + id));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long id) {
        groupRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        groupRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<MessageResponse> joinLeaveGroup(Long id, Authentication authentication, int method) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(500).body(new MessageResponse("ERROR: Server error"));
        }

        var targetGroup = groupRepository.findById(id);
        if (targetGroup.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Invalid request!"));
        }

        var groups = user.get().getGroups();
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
        user.get().setGroups(groups);

        userRepository.save(user.get());
        var methodName = method == 0 ? "joined" : "left";
        return ResponseEntity.ok(new MessageResponse("Successfully " + methodName));
    }
}
