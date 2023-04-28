package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    public RoleEntity findByProjectAndUser(ProjectEntity project, UserEntity user);
}
