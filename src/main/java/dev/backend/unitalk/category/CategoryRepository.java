package dev.backend.unitalk.category;

import dev.backend.unitalk.group.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long>
{
    List<Category> findByGroup(Group group);
}
