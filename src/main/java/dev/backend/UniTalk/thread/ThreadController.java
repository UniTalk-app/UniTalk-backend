package dev.backend.UniTalk.thread;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import dev.backend.UniTalk.group.GroupException;
import dev.backend.UniTalk.group.Group;
import dev.backend.UniTalk.group.GroupRepository;
import dev.backend.UniTalk.category.CategoryRepository;
import dev.backend.UniTalk.category.Category;
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


@RestController
@RequestMapping("/api/group")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ThreadController {

    private final ThreadRepository threadRepository;
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    public ThreadController(ThreadRepository threadRepository,
                            GroupRepository groupRepository,
                            CategoryRepository categoryRepository) {
        this.threadRepository = threadRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository=categoryRepository;
    }

    @GetMapping("/{idGroup}/thread/all")
    public CollectionModel<EntityModel<Thread>> all(@PathVariable Long idGroup) {

        Group group = groupRepository.findById(idGroup).orElseThrow(() -> new GroupException(idGroup));

        List<EntityModel<Thread>> threads = threadRepository.findByGroup(group).stream()
                .map(thread -> EntityModel.of(thread,
                        linkTo(methodOn(ThreadController.class).one(idGroup, thread.getThread_id())).withSelfRel(),
                        linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("threads")))
                .collect(Collectors.toList());

        return CollectionModel.of(threads, linkTo(methodOn(ThreadController.class).all(idGroup)).withSelfRel());
    }

    @GetMapping("/{idGroup}/thread/{idThread}")
    public EntityModel<Thread> one(@PathVariable Long idGroup, @PathVariable Long idThread) {

        Group group = groupRepository.findById(idGroup).orElseThrow(() -> new GroupException(idGroup));

        Thread thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ThreadException(idThread));

        if(thread.getGroup() != group) throw new ThreadException(idThread);

        return EntityModel.of(thread,
                linkTo(methodOn(ThreadController.class).one(idGroup, idThread)).withSelfRel(),
                linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("thread"));
    }

    @PostMapping("/{idGroup}/thread")
    public Thread newThread(@RequestBody Thread newThread, @PathVariable Long idGroup) {

        Thread thread = new Thread(newThread.getTitle(), newThread.getCreator_id(),
                null, null, newThread.getLast_reply_author_id(),
                newThread.getCreation_timestamp(), newThread.getLast_reply_timestamp());

        if(newThread.getCat_id()!=null)
        {
            Optional<Category> category=categoryRepository.findById(newThread.getCat_id());
            if(category.isPresent())
            {
                thread.setCategory(category.get());
                thread.setCat_id(newThread.getCat_id());
            }
        }

        return groupRepository.findById(idGroup).map(group -> {
            thread.setGroup(group);
            return threadRepository.save(thread);
        }).orElseThrow(() -> new GroupException(idGroup));
    }


    @PutMapping("/{idGroup}/thread/{idThread}")
    public Thread replaceThread(@RequestBody Thread newThread,
                                @PathVariable Long idGroup,
                                @PathVariable Long idThread) {

        Group group = groupRepository.findById(idGroup).orElseThrow(() -> new GroupException(idGroup));

        Thread thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ThreadException(idThread));

        thread.setTitle(newThread.getTitle());
        thread.setCreator_id(newThread.getCreator_id());
        thread.setGroup(group);
        thread.setLast_reply_author_id(newThread.getLast_reply_author_id());
        thread.setLast_reply_timestamp(newThread.getLast_reply_timestamp());
        thread.setCreation_timestamp(newThread.getCreation_timestamp());

        if(newThread.getCat_id()!=null)
        {
            Optional<Category> category=categoryRepository.findById(newThread.getCat_id());
            if(category.isPresent())
            {
                thread.setCategory(category.get());
                thread.setCat_id(newThread.getCat_id());
            }
        }

        return threadRepository.save(thread);
    }

    @DeleteMapping("/{idGroup}/thread/{idThread}")
    public void deleteThread(@PathVariable Long idGroup, @PathVariable Long idThread) {
        Group group = groupRepository.findById(idGroup).orElseThrow(() -> new GroupException(idGroup));

        Thread thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ThreadException(idThread));

        threadRepository.deleteById(idThread);
    }
}