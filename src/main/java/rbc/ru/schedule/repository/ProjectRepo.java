package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ProjectEntity;

import java.util.Set;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
    Set<ProjectEntity> findAllByNameStartingWithOrderByName(String name);

    @Query(
            value="select * from project where id in (select distinct project_id from project_tag where tag_id = :tag_id)",
            nativeQuery = true)
    Set<ProjectEntity> findByTag(Long tag_id);
    @Query(
            value="select * from project where id in (select distinct project_id from roles where user_id = :user_id)",
            nativeQuery = true)
    Set<ProjectEntity> findByUser(Long user_id);
}
