package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ResultEntity;

import java.util.Optional;

@Repository
public interface ResultRepo extends JpaRepository<ResultEntity, Long> {
    Optional<ResultEntity> findByTodoId(Long todo_id);
}
