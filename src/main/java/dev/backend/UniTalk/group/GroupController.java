package dev.backend.UniTalk.group;

import java.util.List;

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
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
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
    public CollectionModel<EntityModel<Group>> all() {

        List<EntityModel<Group>> groups = groupControllerService.all();
        return CollectionModel.of(groups, linkTo(methodOn(GroupController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Group> one(@PathVariable Long id) {

        Group group = groupControllerService.one(id);

        return EntityModel.of(group,
                linkTo(methodOn(GroupController.class).one(id)).withSelfRel(),
                linkTo(methodOn(GroupController.class).all()).withRel("group"));
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
}