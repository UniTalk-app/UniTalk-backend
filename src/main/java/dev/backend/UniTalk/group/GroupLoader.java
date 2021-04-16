//========================================================================
// this is only used to insert data into the DB while starting the program
//========================================================================

package dev.backend.UniTalk.group;

import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.thread.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class GroupLoader implements CommandLineRunner {
    private final GroupRepository gRepository;
    private final ThreadRepository tRepository;

    @Autowired
    public GroupLoader(GroupRepository gRepository,
                       ThreadRepository tRepository) {
        this.gRepository = gRepository;
        this.tRepository = tRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Group g = new Group("GroupTitle", 1L, new Timestamp(System.currentTimeMillis()));
        this.gRepository.save(g);
        this.tRepository.save(new Thread("ThreadTitle", 1L, 1L, g,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

    }
}