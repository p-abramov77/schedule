package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.ProjectRepo;
import rbc.ru.schedule.repository.UserRepo;
import rbc.ru.schedule.validator.UserValidator;

import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    UserRepo userRepo;



    @Override
    public ProjectEntity getById(Long id, String principal) {
        ProjectEntity project = projectRepo.findById(id).orElse(null);
        if(project != null) {
            project.setProducer(isProducer(project, principal));
        }
        return project;
    }

    @Override
    public Long save(ProjectEntity projectEntity) {
        ProjectEntity project = projectRepo.save(projectEntity);
        return project.getId();
    }
    @Override
    public Set<ProjectEntity> getByName(String name, String principal) {
        Set<ProjectEntity> projectEntities =  projectRepo.findAllByNameStartingWithOrderByName(name);
        for(ProjectEntity project : projectEntities) {
            project.setProducer(isProducer(project, principal));
        }
        return projectEntities;
    }

    public Boolean isProducer(ProjectEntity project, String principal) {
        if(principal.equals(UserValidator.login)) return true; // superuser

        Long principal_id = userRepo.findByUsername(principal).getId();
        for(RoleEntity role : project.getRoleEntities()) {
            if(principal_id == role.getUser().getId() && role.isProducer()) return true;
        }
        return false;
    }

    @Override
    public Set<ProjectEntity> getByUsername(String username, String principal) {
        Set<ProjectEntity> projectEntities = projectRepo.findByUser(userRepo.findByUsername(username).getId());
        for(ProjectEntity project : projectEntities) {
            project.setProducer(isProducer(project, principal));
        }
        return projectEntities;
    }
    @Override
    public Set<ProjectEntity> getByTag(Long id, String principal) {
        Set<ProjectEntity> projectEntities = projectRepo.findByTag(id);
        for(ProjectEntity project : projectEntities) {
            project.setProducer(isProducer(project, principal));
        }
        return projectEntities;
    }
}
