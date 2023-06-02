package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ResultEntity;

import java.util.Set;

@Service
public interface ResultService {
    Set<ResultEntity> listOfResults(Long todo_id);
    ResultEntity getById(Long id);
    void save(ResultEntity resultEntity);

}
