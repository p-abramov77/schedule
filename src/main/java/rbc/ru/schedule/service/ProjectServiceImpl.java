package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.ProjectRepo;
import rbc.ru.schedule.repository.UserRepo;
import rbc.ru.schedule.validator.UserValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<ProjectEntity> sortingTODO(Set<ProjectEntity> projectEntities) {
        for(ProjectEntity project : projectEntities) {
            project.setTodos(
                    project.getTodos().stream()
                            .sorted(new Comparator<ToDoEntity>() {
                                @Override
                                public int compare(ToDoEntity o1, ToDoEntity o2) {
                                    return o1.getStart().compareTo(o2.getStart());
                                }
                            })
                            .collect(Collectors.toSet()));
        }
        return projectEntities;
    }
    @Override
    public Set<ProjectEntity> getByName(String name, String principal) {
        Set<ProjectEntity> projectEntities =  projectRepo.findAllByNameStartingWithOrderByStart(name);

        for(ProjectEntity project : projectEntities) {
            project.setProducer(isProducer(project, principal));
        }
        projectEntities = sortingTODO(projectEntities);

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
    public Set<ProjectEntity> findInPeriod(LocalDateTime start, LocalDateTime stop) {
        return projectRepo.findInPeriod(start, stop);
    }

    @Override
    public Set<ProjectEntity> findInDay(LocalDate day, Set<ProjectEntity> all) {

        Date date = Date.valueOf(day);
        return all.stream()
                .filter(x -> ! (x.getStop().before(date) || x.getStart().after(date) ) )
                .sorted(new Comparator<ProjectEntity>() {
                    @Override
                    public int compare(ProjectEntity o1, ProjectEntity o2) {
                        return o1.getStart().compareTo(o2.getStart());
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isNotLastProducer(ProjectEntity project) {
        int countOfProduccers = 0;
        for(RoleEntity role : project.getRoleEntities()) {
            if(role.isProducer()) countOfProduccers++;
        }
        System.out.println(countOfProduccers > 1);
        return countOfProduccers > 1;
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
