//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.UniTalk.thread;

import dev.backend.UniTalk.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findByGroup(Group group);
}