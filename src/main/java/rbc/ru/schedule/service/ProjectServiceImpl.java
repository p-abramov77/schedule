package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.repository.ProjectRepo;

import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    ProjectRepo projectRepo;
    @Override
    public Set<ProjectEntity> getByName(String name) {
        return projectRepo.findAllByNameStartingWithOrderByName(name);
    }

    @Override
    public Set<ProjectEntity> getByTag(Long id) {
        return projectRepo.findByTag(id);
    }

    @Override
    public ProjectEntity getById(Long id) {
        return projectRepo.findById(id).orElse(null);
    }

    @Override
    public void save(ProjectEntity projectEntity) {
        projectRepo.save(projectEntity);
    }

    @Override
    public Set<ProjectEntity> getByUsername(String name) {
        return null; //TODO
    }
}
