package dev.backend.unitalk.group;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class GroupControllerService {

    private final GroupRepository repository;
    private static final String NOT_FOUND_GROUP = "Not found group with id = ";

    public GroupControllerService(GroupRepository repository) {
        this.repository = repository;
    }

    public List<EntityModel<Group>> all() {

        return repository.findAll().stream()
                .map(group -> EntityModel.of(group,
                        linkTo(methodOn(GroupController.class).one(group.getGroupId())).withSelfRel(),
                        linkTo(methodOn(GroupController.class).all()).withRel("groups")))
                .collect(Collectors.toList());
    }

    public Group one(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + id));
    }

    public Group newGroup(Group newGroup) {

        var group = new Group(newGroup.getGroupName(), newGroup.getCreatorId(),
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
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + id));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long id) {


        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
