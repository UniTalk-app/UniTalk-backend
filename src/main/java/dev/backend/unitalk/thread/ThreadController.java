package dev.backend.unitalk.thread;

import dev.backend.unitalk.payload.request.ThreadRequest;
import dev.backend.unitalk.payload.response.MessageResponse;
import dev.backend.unitalk.user.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


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
    public Thread newThread(@Valid @RequestBody ThreadRequest newThread, @PathVariable Long idGroup, @AuthenticationPrincipal User user) {

        return threadControllerService.newThread(newThread, idGroup, user);
    }


    @PutMapping("/{idGroup}/thread/{idThread}")
    public Thread replaceThread(@Valid @RequestBody ThreadRequest newThread,
                                @PathVariable Long idGroup,
                                @PathVariable Long idThread) {

        return threadControllerService.replaceThread(newThread, idGroup, idThread);
    }

    @DeleteMapping("/{idGroup}/thread/{idThread}")
    public ResponseEntity<MessageResponse> deleteOne(@PathVariable Long idGroup, @PathVariable Long idThread, @AuthenticationPrincipal User user) throws Exception {
        return threadControllerService.deleteOne(idThread, user);
    }

    @DeleteMapping("/{idGroup}/thread/")
    public ResponseEntity<HttpStatus> deleteAll(@PathVariable Long idGroup) {
        return threadControllerService.deleteAll();
    }
}