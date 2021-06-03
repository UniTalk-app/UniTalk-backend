package dev.backend.unitalk.thread;

import dev.backend.unitalk.category.CategoryRepository;
import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.group.GroupRepository;
import dev.backend.unitalk.payload.request.ThreadRequest;
import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.role.ERole;
import dev.backend.unitalk.user.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class ThreadControllerService {
    private static final String NOT_FOUND_GROUP = "Not found group with id = ";
    private static final String NOT_FOUND_THREAD = "Not found thread with id = ";
    private static final String NOT_FOUND_CATEGORY = "Not found category with id = ";
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
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        return threadRepository.findByGroup(group).stream()
                .map(thread -> EntityModel.of(thread,
                        linkTo(methodOn(ThreadController.class).one(idGroup, thread.getThreadId())).withSelfRel(),
                        linkTo(methodOn(ThreadController.class).all(idGroup)).withRel("threads")))
                .collect(Collectors.toList());
    }

    public Thread one(Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_THREAD + idThread));

        if(thread.getGroup() != group)
            throw new ResourceNotFoundException(NOT_FOUND_THREAD + idThread);

        return thread;
    }

    public Thread newThread(ThreadRequest newThread, Long idGroup, User user) throws Exception {

        var category = newThread.getCategoryId() == -1
                ? null
                : categoryRepository.findById(newThread.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CATEGORY + newThread.getCategoryId()));

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));
        if (!user.getGroups().contains(group)) {
            throw new UserAuthenticationException("User not in group");
        }

        return threadRepository.save(new Thread(
                newThread.getTitle(), user.getId(), group, category, -1L, new Timestamp(new Date().getTime()), null
        ));
    }

    public Thread replaceThread(ThreadRequest newThread, Long idGroup, Long idThread) {

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_THREAD + idThread));

        var category = newThread.getCategoryId() == -1
                ? null
                : categoryRepository.findById(newThread.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CATEGORY + newThread.getCategoryId()));

        thread.setCategory(category);
        thread.setTitle(newThread.getTitle());
        return threadRepository.save(thread);
    }

    public ResponseEntity<MessageResponse> deleteOne(Long idThread, User user) throws Exception {
        var thread = threadRepository.findById(idThread)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_THREAD + idThread));

        if (
                !thread.getCreatorId().equals(user.getId()) &&
                        user.getRoles().stream().noneMatch(role -> role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR)
        ) {
            throw new UserAuthenticationException("No rights to delete this resource");
        }

        threadRepository.deleteById(idThread);
        return ResponseEntity.ok().body(new MessageResponse("Thread successfully deleted"));
    }

    public ResponseEntity<HttpStatus> deleteAll() {
        threadRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
