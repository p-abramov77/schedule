package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.CommentEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.repository.CommentRepo;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepo commentRepo;

    @Override
    public List<CommentEntity> findAll(ToDoEntity toDoEntity) {

        return commentRepo.findByToDoEntityOrderByDateTime(toDoEntity);
    }

    @Override
    public void save(CommentEntity commentEntity) {

        commentRepo.save(commentEntity);
    }

    @Override
    public CommentEntity getById(Long id) {

        return commentRepo.findById(id).orElse(null);
    }
}
