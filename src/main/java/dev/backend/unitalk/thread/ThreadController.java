package dev.backend.unitalk.thread;

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


@RestController
@RequestMapping("/api/group")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ThreadController {

    private final ThreadControllerService threadControllerService;

    public ThreadController(ThreadControllerService threadControllerService) {
        this.threadControllerService=threadControllerService;
    }

    @GetMapping("/{idGroup}/thread/all")
    public CollectionModel<EntityModel<Thread>> all(@PathVariable Long idGroup) {

        List<EntityModel<Thread>> threads = threadControllerService.all(idGroup);

        return CollectionModel.of(threads, linkTo(methodOn(ThreadController.class).all(idGroup)).withSelfRel());
    }

    @GetMapping("/{idGroup}/thread/{idThread}")
    public EntityModel<Thread> one(@PathVariable Long idGroup, @PathVariable Long idThread) {

        var thread = threadControllerService.one(idGroup, idThread);

        return EntityModel.of(thread,
                linkTo(methodOn(ThreadController.class).one(idGroup, idThread)).withSelfRel(),
                linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("thread"));
    }

    @PostMapping("/{idGroup}/thread")
    public Thread newThread(@Valid @RequestBody Thread newThread, @PathVariable Long idGroup) {

        return threadControllerService.newThread(newThread, idGroup);
    }


    @PutMapping("/{idGroup}/thread/{idThread}")
    public Thread replaceThread(@Valid @RequestBody Thread newThread,
                                @PathVariable Long idGroup,
                                @PathVariable Long idThread) {

        return threadControllerService.replaceThread(newThread, idGroup, idThread);
    }

    @DeleteMapping("/{idGroup}/thread/{idThread}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long idGroup, @PathVariable Long idThread) {
        return threadControllerService.deleteOne(idGroup, idThread);
    }

    @DeleteMapping("/{idGroup}/thread/")
    public ResponseEntity<HttpStatus> deleteAll(@PathVariable Long idGroup) {
        return threadControllerService.deleteAll(idGroup);
    }
}