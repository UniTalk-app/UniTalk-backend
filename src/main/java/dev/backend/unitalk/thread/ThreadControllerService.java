package dev.backend.unitalk.thread;

import dev.backend.unitalk.category.Category;
import dev.backend.unitalk.category.CategoryRepository;
import dev.backend.unitalk.exception.ResourceNotFoundException;
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
    private final static String notFoundGroup = "Not found group with id = ";
    private final static String notFoundThread = "Not found thread with id = ";
    private final static String notFoundCategory = "Not found category with id = ";


    public ThreadControllerService(ThreadRepository threadRepository,
                            GroupRepository groupRepository,
                            CategoryRepository categoryRepository) {
        this.threadRepository = threadRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository=categoryRepository;
    }

    public List<EntityModel<Thread>> all(Long idGroup) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException( notFoundGroup + idGroup));

        return threadRepository.findByGroup(group).stream()
                .map(thread -> EntityModel.of(thread,
                        linkTo(methodOn(ThreadController.class).one(idGroup, thread.getThreadId())).withSelfRel(),
                        linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("threads")))
                .collect(Collectors.toList());
    }

    public Thread one(Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundGroup + idGroup));

        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundThread + idThread));

        if(thread.getGroup() != group)
            throw new ResourceNotFoundException(notFoundThread + idThread);

        return thread;
    }

    public Thread newThread(Thread newThread, Long idGroup) {

        var thread = new Thread(newThread.getTitle(), newThread.getCreatorId(),
                null, null, newThread.getLastReplyAuthorId(),
                newThread.getCreationTimestamp(), newThread.getLastReplyTimestamp());

        if(newThread.getCatId()!=null)
        {
            var category = categoryRepository.findById(newThread.getCatId())
                    .orElseThrow(() -> new ResourceNotFoundException(notFoundCategory + newThread.getCatId()));

            thread.setCategory(category);
            thread.setCatId(newThread.getCatId());

        } else
            throw new ResourceNotFoundException("Category field cannot be null");

        return groupRepository.findById(idGroup).map(group -> {
            thread.setGroup(group);
            return threadRepository.save(thread);
        }).orElseThrow(() -> new ResourceNotFoundException(notFoundGroup+ idGroup));
    }

    public Thread replaceThread(Thread newThread, Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundGroup + idGroup));


        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundThread + idThread));

        thread.setTitle(newThread.getTitle());
        thread.setCreatorId(newThread.getCreatorId());
        thread.setGroup(group);
        thread.setLastReplyAuthorId(newThread.getLastReplyAuthorId());
        thread.setLastReplyTimestamp(newThread.getLastReplyTimestamp());
        thread.setCreationTimestamp(newThread.getCreationTimestamp());

        if(newThread.getCatId()!=null)
        {
            var category = categoryRepository.findById(newThread.getCatId())
                    .orElseThrow(() -> new ResourceNotFoundException(notFoundCategory + newThread.getCatId()));

            thread.setCategory(category);
            thread.setCatId(newThread.getCatId());

        } else
            throw new ResourceNotFoundException("Category field cannot be null");

        return threadRepository.save(thread);
    }

    public ResponseEntity<HttpStatus> deleteOne(Long idThread) {

        threadRepository.deleteById(idThread);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {


        threadRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
