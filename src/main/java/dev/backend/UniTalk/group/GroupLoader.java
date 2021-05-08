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

        Category c=new Category("CAT1", g, new Timestamp(System.currentTimeMillis()));
        this.cRepository.save(c);
        this.cRepository.save(new Category("CAT2", g, new Timestamp(System.currentTimeMillis() - 10000000)));

        Thread t=new Thread("ThreadTitle1", 1L, c.getCategoryId(), g,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        t.setCategory(c);
        this.tRepository.save(t);

        this.tRepository.save(new Thread("ThreadTitle2", 1L,null, g,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
    }
}