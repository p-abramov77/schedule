package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.RoleRepo;

import javax.management.relation.Role;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepo roleRepo;
    @Override
    public void save(RoleEntity roleEntity) {
        roleRepo.save(roleEntity);
    }

    @Override
    public RoleEntity findByProjectAndUser(ProjectEntity project, UserEntity user) {
        return roleRepo.findByProjectAndUser(project, user);
    }
}
