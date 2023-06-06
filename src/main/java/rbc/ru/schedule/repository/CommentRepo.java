package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rbc.ru.schedule.entity.CommentEntity;
import rbc.ru.schedule.entity.ToDoEntity;

import java.util.List;

public interface CommentRepo extends JpaRepository <CommentEntity, Long>{
    List<CommentEntity> findByTodoOrderByDateTime(ToDoEntity toDoEntity);
}
