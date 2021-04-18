//========================================================================
// this is only used to insert data into the DB while starting the program
//========================================================================

package dev.backend.UniTalk.group;

import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.thread.ThreadRepository;
import dev.backend.UniTalk.category.Category;
import dev.backend.UniTalk.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class GroupLoader implements CommandLineRunner {
    private final GroupRepository gRepository;
    private final ThreadRepository tRepository;
    private final CategoryRepository cRepository;

    @Autowired
    public GroupLoader(GroupRepository gRepository,
                       ThreadRepository tRepository,
                       CategoryRepository cRepository) {
        this.gRepository = gRepository;
        this.tRepository = tRepository;
        this.cRepository = cRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Group g = new Group("GroupTitle", 1L, new Timestamp(System.currentTimeMillis()));
        this.gRepository.save(g);
        this.tRepository.save(new Thread("ThreadTitle", 1L, 1L, g,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

        this.cRepository.save(new Category("CAT1", g, new Timestamp(System.currentTimeMillis())));
        this.cRepository.save(new Category("CAT2", g, new Timestamp(System.currentTimeMillis() - 10000000)));
    }
}