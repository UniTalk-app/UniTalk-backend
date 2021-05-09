//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.UniTalk.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatorId(Long id);
}