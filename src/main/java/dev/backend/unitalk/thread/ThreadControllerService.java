package dev.backend.unitalk.thread;

import dev.backend.unitalk.category.Category;
import dev.backend.unitalk.category.CategoryRepository;
import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.group.Group;
import dev.backend.unitalk.group.GroupRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class ThreadControllerService {
    private final ThreadRepository threadRepository;
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    public ThreadControllerService(ThreadRepository threadRepository,
                            GroupRepository groupRepository,
                            CategoryRepository categoryRepository) {
        this.threadRepository = threadRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository=categoryRepository;
    }

    public List<EntityModel<Thread>> all(Long idGroup) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));

        return threadRepository.findByGroup(group).stream()
                .map(thread -> EntityModel.of(thread,
                        linkTo(methodOn(ThreadController.class).one(idGroup, thread.getThreadId())).withSelfRel(),
                        linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("threads")))
                .collect(Collectors.toList());
    }

    public Thread one(Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));

        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + idThread));

        if(thread.getGroup() != group)
            throw new ResourceNotFoundException("Not found thread with id = " + idThread);

        return thread;
    }

    public Thread newThread(Thread newThread, Long idGroup) {

        var thread = new Thread(newThread.getTitle(), newThread.getCreatorId(),
                null, null, newThread.getLastReplyAuthorId(),
                newThread.getCreationTimestamp(), newThread.getLastReplyTimestamp());

        if(newThread.getCatId()!=null)
        {
            Category category = categoryRepository.findById(newThread.getCatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Not found category with id = " + newThread.getCatId()));

            thread.setCategory(category);
            thread.setCatId(newThread.getCatId());

        } else
            throw new ResourceNotFoundException("Category field cannot be null");

        return groupRepository.findById(idGroup).map(group -> {
            thread.setGroup(group);
            return threadRepository.save(thread);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));
    }

    public Thread replaceThread(Thread newThread, Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));


        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + idThread));

        thread.setTitle(newThread.getTitle());
        thread.setCreatorId(newThread.getCreatorId());
        thread.setGroup(group);
        thread.setLastReplyAuthorId(newThread.getLastReplyAuthorId());
        thread.setLastReplyTimestamp(newThread.getLastReplyTimestamp());
        thread.setCreationTimestamp(newThread.getCreationTimestamp());

        if(newThread.getCatId()!=null)
        {
            var category = categoryRepository.findById(newThread.getCatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Not found category with id = " + newThread.getCatId()));

            thread.setCategory(category);
            thread.setCatId(newThread.getCatId());

        } else
            throw new ResourceNotFoundException("Category field cannot be null");

        return threadRepository.save(thread);
    }

    public ResponseEntity<HttpStatus> deleteOne(Long idGroup, Long idThread) {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));

        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + idThread));

        threadRepository.deleteById(idThread);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll(Long idGroup) {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException("Not found group with id = " + idGroup));

        threadRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
