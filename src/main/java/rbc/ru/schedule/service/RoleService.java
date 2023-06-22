package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.RoleEntity;

@Service
public interface RoleService {
    public void save(RoleEntity roleEntity);
    public void removeByProjectAndUser(Long project_id, Long user_id);
    public long countOfExecutors(Long project_id);
}
