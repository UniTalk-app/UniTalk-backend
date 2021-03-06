package dev.backend.unitalk.group;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.payload.request.GroupRequest;
import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.user.User;
import dev.backend.unitalk.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class GroupControllerService {
    private static final String NOT_FOUND_GROUP = "Not found group with id = ";
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupControllerService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Group> all(User user) {
        return new ArrayList<>(user.getGroups());
    }

    public Group one(Long id) {

        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + id));
    }

    public Group newGroup(GroupRequest newGroup, @AuthenticationPrincipal User user) {

        var group = new Group(newGroup.getName(), user.getId(), new Timestamp(new Date().getTime()));

        var resVal = groupRepository.save(group);
        user.getGroups().add(group);
        user.setGroups(user.getGroups());
        userRepository.save(user);

        return resVal;
    }

    public Group replaceGroup(GroupRequest newGroup, Long id) {

        return groupRepository.findById(id)
                .map(group -> {
                    group.setGroupName(newGroup.getName());
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
}
