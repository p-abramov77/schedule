package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ResultEntity;

import java.util.Set;

@Repository
public interface ResultRepo extends JpaRepository<ResultEntity, Long> {
    @Query(value = "select * from results where todo_id=:todo_id order by date_time",
            nativeQuery = true)
    Set<ResultEntity> findAllByTodoId(Long todo_id);
}
