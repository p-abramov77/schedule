package rbc.ru.schedule.service;

import com.sun.tools.javac.comp.Todo;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ToDoEntity;

import java.util.Set;

@Service
public interface ToDoService {
    Set<ToDoEntity> findAllByProjectId(Long projectId);
    ToDoEntity findById(Long id);
    void deleteById(Long id);
    void save(ToDoEntity toDoEntity);
    public boolean isPeriod(ToDoEntity toDoEntity);
}
