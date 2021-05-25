//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.unitalk.thread;

import dev.backend.unitalk.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findByGroup(Group group);
}