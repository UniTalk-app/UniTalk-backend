package dev.backend.UniTalk.controller;

import java.util.List;
import dev.backend.UniTalk.exception.ThreadException;
import dev.backend.UniTalk.model.Thread;
import dev.backend.UniTalk.repository.ThreadRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/thread")
public class ThreadController {

    private final ThreadRepository repository;

    public ThreadController(ThreadRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    public List<Thread> all() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Thread one(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ThreadException(id));
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