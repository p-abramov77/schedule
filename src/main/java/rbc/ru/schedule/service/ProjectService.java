package rbc.ru.schedule.service;

import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public interface ProjectService {
    Set<ProjectEntity> getByTag(Long id, LocalDateTime start, LocalDateTime stop, String principal);
    ProjectEntity getById(Long id, String principal);
    Long save(ProjectEntity projectEntity);

    Set<ProjectEntity> getByName(String name, LocalDateTime start, LocalDateTime stop, boolean oneGroup, Long group_id, String principal);

    Set<ProjectEntity> getByUsername(String name, LocalDateTime start, LocalDateTime stop, String principal);
    Set<ProjectEntity> findInPeriod(LocalDateTime start, LocalDateTime stop, boolean oneGroup, Long group_id);
    Set<ProjectEntity> findInDay(LocalDate day, Set<ProjectEntity> all);
    boolean isNotLastProducer(ProjectEntity project);
    public boolean isPeriod(ProjectEntity project);
}