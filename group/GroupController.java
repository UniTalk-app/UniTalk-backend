package dev.backend.UniTalk.group;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new GroupException(id));

        return EntityModel.of(group,
                linkTo(methodOn(GroupController.class).one(id)).withSelfRel(),
                linkTo(methodOn(GroupController.class).all()).withRel("group"));
    }

    @PostMapping("/")
    public Group newGroup(@RequestBody Group newGroup) {
        return repository.save(newGroup);
    }


    @PutMapping("/{id}")
    public Group replaceGroup(@RequestBody Group newGroup, @PathVariable Long id) {

        return repository.findById(id)
                .map(group -> {
                    group.setGroup_name(newGroup.getGroup_name());
                    group.setCreator_id(newGroup.getCreator_id());
                    group.setCreation_timestamp(newGroup.getCreation_timestamp());
                    return repository.save(group);
                })
                .orElseGet(() -> {
                    newGroup.setGroup_id(id);
                    return repository.save(newGroup);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        repository.deleteById(id);
    }
}