package dev.backend.UniTalk.group;

import dev.backend.UniTalk.exception.ResourceNotFoundException;
import dev.backend.UniTalk.payload.response.MessageResponse;
import dev.backend.UniTalk.security.services.UserDetailsImpl;
import dev.backend.UniTalk.user.UserRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));
    }

    public Group newGroup(Group newGroup) {

        Group group = new Group(newGroup.getGroupName(), newGroup.getCreatorId(),
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
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long id) {
        groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));

        groupRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        groupRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<MessageResponse> joinLeaveGroup(Long id, Authentication authentication, int method) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var user = userRepository.findByUsername(userDetails.getUsername()).get();
        if (groupRepository.findById(id).isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Invalid request!"));
        }
        var targetGroup = groupRepository.findById(id).get();
        var groups = user.getGroups();
        if (
                method == 0 && groups.contains(targetGroup) ||
                method != 0 && !groups.contains(targetGroup)
        ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Invalid request!"));
        }

        if (method == 0) {
            groups.add(targetGroup);
        }
        else {
            groups.remove(targetGroup);
        }
        user.setGroups(groups);

        userRepository.save(user);
        var methodName = method == 0 ? "joined" : "left";
        return ResponseEntity.ok(new MessageResponse("Successfully " + methodName));
    }
}
