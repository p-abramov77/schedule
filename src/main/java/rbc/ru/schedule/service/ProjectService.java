package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;

import java.util.Set;

@Service
public interface ProjectService {
    Set<ProjectEntity> getByName(String name);
    Set<ProjectEntity> getByTag(Long id);
    ProjectEntity getById(Long id);
    void save(ProjectEntity projectEntity);
    Set<ProjectEntity> getByUsername(String name);
}