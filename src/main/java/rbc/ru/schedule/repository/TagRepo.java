package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.TagEntity;

import java.util.Set;

@Repository
public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Set<TagEntity> findAllByNameStartingWithOrderByName(String name);
    Set<TagEntity> findByIdNotIn(Set<Long> tags);
}
