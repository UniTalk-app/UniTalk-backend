package dev.backend.UniTalk.repository;

import dev.backend.UniTalk.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository <Group, Long> {

}
