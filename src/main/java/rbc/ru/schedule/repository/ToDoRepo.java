package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rbc.ru.schedule.entity.ToDoEntity;

import java.util.Set;

public interface ToDoRepo extends JpaRepository<ToDoEntity, Long> {

    @Query(value = "select * from todos where project_id = :project_id order by start", nativeQuery = true)
    Set<ToDoEntity> findAllByProjectIdOrderByStart(Long project_id);

}
