package dev.backend.UniTalk.group;

import java.util.List;
import java.util.stream.Collectors;

import dev.backend.UniTalk.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupRepository repository;

    public GroupController(GroupRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Group>> all() {

        List<EntityModel<Group>> groups = repository.findAll().stream()
                .map(group -> EntityModel.of(group,
                        linkTo(methodOn(GroupController.class).one(group.getGroup_id())).withSelfRel(),
                        linkTo(methodOn(GroupController.class).all()).withRel("groups")))
                .collect(Collectors.toList());

        return CollectionModel.of(groups, linkTo(methodOn(GroupController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Group> one(@PathVariable Long id) {

        Group group = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));

        return EntityModel.of(group,
                linkTo(methodOn(GroupController.class).one(id)).withSelfRel(),
                linkTo(methodOn(GroupController.class).all()).withRel("group"));
    }

    @PostMapping("/")
    public Group newGroup(@Valid @RequestBody Group newGroup) {

        Group group = new Group(newGroup.getGroup_name(), newGroup.getCreator_id(),
                newGroup.getCreation_timestamp());

        return repository.save(group);
    }


    @PutMapping("/{id}")
    public Group replaceGroup(@Valid @RequestBody Group newGroup, @PathVariable Long id) {

        return repository.findById(id)
                .map(group -> {
                    group.setGroup_name(newGroup.getGroup_name());
                    group.setCreator_id(newGroup.getCreator_id());
                    group.setCreation_timestamp(newGroup.getCreation_timestamp());
                    return repository.save(group);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + id));

        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAll() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}