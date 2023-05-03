package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ProjectEntity;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;

import javax.transaction.Transactional;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ROLES WHERE project_id = :project_id AND user_id = :user_id", nativeQuery = true)
    public void removeByProjectAndUser(Long project_id, Long user_id);
}
