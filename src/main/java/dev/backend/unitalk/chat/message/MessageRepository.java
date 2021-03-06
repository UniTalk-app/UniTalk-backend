//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.unitalk.chat.message;

import dev.backend.unitalk.thread.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>
{
    List<Message> findByThread(Thread thread);
}