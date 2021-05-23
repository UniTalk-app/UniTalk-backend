//============================================
// NOTE! JpaRepository contains CrudRepository
//============================================

package dev.backend.unitalk.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {}