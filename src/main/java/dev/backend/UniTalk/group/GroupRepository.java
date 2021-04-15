//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.UniTalk.group;

import dev.backend.UniTalk.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {}