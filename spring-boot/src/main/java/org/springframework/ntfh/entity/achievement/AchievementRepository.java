package org.springframework.ntfh.entity.achievement;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    Optional<Achievement> findOptionalByName(@Param("name") String name);
}
