package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.repository.ToDoRepo;

import java.util.Set;

@Service
public class ToDoServiceImpl implements ToDoService{
    @Autowired
    ToDoRepo toDoRepo;

    @Override
    public Set<ToDoEntity> findAllByProjectId(Long projectId) {
        return toDoRepo.findAllByProjectIdOrderByStart(projectId);
    }

    @Override
    public ToDoEntity findById(Long id) {
        return toDoRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        toDoRepo.deleteById(id);
    }

    @Override
    public void save(ToDoEntity toDoEntity) {
        toDoRepo.save(toDoEntity);
    }

    @Override
    public boolean isPeriod(ToDoEntity toDoEntity) {
        return toDoEntity.getStart().isBefore(toDoEntity.getStop().plusMinutes(1L));
    }
}
