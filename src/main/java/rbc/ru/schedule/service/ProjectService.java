package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public interface ProjectService {
    Set<ProjectEntity> getByName(String name, String principal);
    Set<ProjectEntity> getByTag(Long id, String principal);
    ProjectEntity getById(Long id, String principal);
    Long save(ProjectEntity projectEntity);
    Set<ProjectEntity> getByUsername(String name, String principal);
    Set<ProjectEntity> findInPeriod(LocalDateTime start, LocalDateTime stop);
    Set<ProjectEntity> findInDay(LocalDate day, Set<ProjectEntity> all);
    boolean isNotLastProducer(ProjectEntity project);
    public boolean isPeriod(ProjectEntity project);
}