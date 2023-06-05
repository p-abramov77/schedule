package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ResultEntity;
import rbc.ru.schedule.repository.ResultRepo;

import java.util.Set;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    ResultRepo resultRepo;

    @Override
    public Set<ResultEntity> findByTodoId(Long id) {

        return resultRepo.findByTodoId(id);
    }

    @Override
    public ResultEntity findById(Long id) {
        return resultRepo.findById(id).orElse(null);
    }

    @Override
    public void save(ResultEntity resultEntity) {
        resultRepo.save(resultEntity);
    }
}
