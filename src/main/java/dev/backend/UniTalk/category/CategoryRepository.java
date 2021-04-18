package dev.backend.UniTalk.category;

import dev.backend.UniTalk.group.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long>
{
    List<Category> findByGroup(Group group);
}
