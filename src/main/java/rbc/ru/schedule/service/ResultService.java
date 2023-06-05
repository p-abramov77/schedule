package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ResultEntity;

@Service
public interface ResultService {
    ResultEntity findByTodoId(Long id);
    ResultEntity findById(Long id);
    void save(ResultEntity resultEntity);

}
