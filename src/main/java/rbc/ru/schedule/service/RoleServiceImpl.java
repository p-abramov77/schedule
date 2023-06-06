package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.repository.RoleRepo;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepo roleRepo;
    @Override
    public void save(RoleEntity roleEntity) {

        roleRepo.save(roleEntity);
    }

    @Override
    public void removeByProjectAndUser(Long project_id, Long user_id) {
        roleRepo.removeByProjectAndUser(project_id, user_id);
    }
}
