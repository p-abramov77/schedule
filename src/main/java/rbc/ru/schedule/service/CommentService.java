package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.CommentEntity;
import rbc.ru.schedule.entity.ToDoEntity;

import java.util.List;

@Service
public interface CommentService {
    List<CommentEntity> findAll(ToDoEntity toDoEntity);
    void save(CommentEntity commentEntity);
    CommentEntity getById(Long id);
}
