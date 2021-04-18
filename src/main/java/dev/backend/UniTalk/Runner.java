package dev.backend.UniTalk;

import dev.backend.UniTalk.model.Category;
import dev.backend.UniTalk.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class Runner implements CommandLineRunner
{
    private final CategoryRepository repository;

    @Autowired
    public Runner(CategoryRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception
    {
        this.repository.save(new Category("CAT1", 1L, new Timestamp(System.currentTimeMillis())));
        this.repository.save(new Category("CAT2", 1L, new Timestamp(System.currentTimeMillis() - 10000000)));
    }
}