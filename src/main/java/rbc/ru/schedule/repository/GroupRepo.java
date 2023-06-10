package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.GroupEntity;

import java.util.Set;

@Repository
public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
    Set<GroupEntity> findAllByNameStartingWithOrderByName(String name);
    Set<GroupEntity> findByIdNotIn(Set<Long> groups);

}
