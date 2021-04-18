package dev.backend.UniTalk.repository;

import dev.backend.UniTalk.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long>
{

}
