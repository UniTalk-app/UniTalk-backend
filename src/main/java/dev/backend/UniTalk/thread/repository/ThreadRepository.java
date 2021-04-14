//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.UniTalk.thread.repository;

import dev.backend.UniTalk.thread.model.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {}