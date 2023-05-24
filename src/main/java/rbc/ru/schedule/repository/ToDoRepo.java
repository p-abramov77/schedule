package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rbc.ru.schedule.entity.ToDoEntity;

import java.util.Set;

public interface ToDoRepo extends JpaRepository<ToDoEntity, Long> {

    @Query(value = "select * from todo_s where project_id = :project_id", nativeQuery = true)
    Set<ToDoEntity> findAllByProjectId(Long project_id);

}
