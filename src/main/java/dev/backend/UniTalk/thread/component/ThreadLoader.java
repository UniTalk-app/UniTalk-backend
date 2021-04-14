//========================================================================
// this is only used to insert data into the DB while starting the program
//========================================================================

package dev.backend.UniTalk.thread.component;

import dev.backend.UniTalk.thread.model.Thread;
import dev.backend.UniTalk.thread.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class ThreadLoader implements CommandLineRunner {
    private final ThreadRepository repository;

    @Autowired
    public ThreadLoader(ThreadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.repository.save(new Thread("ThreadTitle", 1, 1, 1,
                1, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
    }
}