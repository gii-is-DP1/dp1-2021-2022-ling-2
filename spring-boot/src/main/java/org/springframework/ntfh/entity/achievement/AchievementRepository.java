package org.springframework.ntfh.entity.achievement;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {

    List<Achievement> findAll();

    @Query("SELECT a FROM Achievement a")
    Page<Achievement> findPage(Pageable pageable);

    Optional<Achievement> findOptionalByName(@Param("name") String name);
}
