package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;

@Service
public interface RoleService {
    public void save(RoleEntity roleEntity);
    public RoleEntity findByProjectAndUser(ProjectEntity project, UserEntity user);
}
