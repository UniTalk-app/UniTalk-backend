package dev.backend.UniTalk.thread.controller;

import java.util.List;
import java.util.stream.Collectors;
import dev.backend.UniTalk.thread.exception.ThreadException;
import dev.backend.UniTalk.thread.model.Thread;
import dev.backend.UniTalk.thread.repository.ThreadRepository;
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
@RequestMapping("/thread")
public class ThreadController {

    private final ThreadRepository repository;

    public ThreadController(ThreadRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Thread>> all() {

        List<EntityModel<Thread>> threads = repository.findAll().stream()
                .map(thread -> EntityModel.of(thread,
                        linkTo(methodOn(ThreadController.class).one(thread.getThread_id())).withSelfRel(),
                        linkTo(methodOn(ThreadController.class).all()).withRel("threads")))
                .collect(Collectors.toList());

        return CollectionModel.of(threads, linkTo(methodOn(ThreadController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Thread> one(@PathVariable Long id) {

    Thread thread = repository.findById(id)
            .orElseThrow(() -> new ThreadException(id));

    return EntityModel.of(thread,
            linkTo(methodOn(ThreadController.class).one(id)).withSelfRel(),
            linkTo(methodOn(ThreadController.class).all()).withRel("thread"));
}

    @PostMapping("/")
    public Thread newThread(@RequestBody Thread newThread) {
        return repository.save(newThread);
    }


    @PutMapping("/{id}")
    public Thread replaceThread(@RequestBody Thread newThread, @PathVariable Long id) {

        return repository.findById(id)
                .map(thread -> {
                    thread.setTitle(newThread.getTitle());
                    thread.setCreator_id(newThread.getCreator_id());
                    thread.setCategory_id(newThread.getCategory_id());
                    thread.setGroup_id(newThread.getGroup_id());
                    thread.setLast_reply_author_id(newThread.getLast_reply_author_id());
                    thread.setLast_reply_timestamp(newThread.getLast_reply_timestamp());
                    thread.setCreation_timestamp(newThread.getCreation_timestamp());
                    return repository.save(thread);
                })
                .orElseGet(() -> {
                    newThread.setThread_id(id);
                    return repository.save(newThread);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}