package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rbc.ru.schedule.entity.ProjectEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
    @Query(value = "select * from project where ( name like %:name% and not (stop < :start or :stop < start) ) order by start",
            nativeQuery = true)
    Set<ProjectEntity> findAllByNameOrderByStart(String name, LocalDateTime start, LocalDateTime stop);

    @Query(
            value="select * from project where id in (select distinct project_id from project_tag where not (stop < :start or :stop < start) and tag_id = :tag_id) order by start",
            nativeQuery = true)
    Set<ProjectEntity> findByTag(Long tag_id, LocalDateTime start, LocalDateTime stop);
    @Query(
            value="select * from project where not (stop < :start or :stop < start) and id in (select distinct project_id from roles where user_id = :user_id) order by start",
            nativeQuery = true)
    Set<ProjectEntity> findByUser(Long user_id, LocalDateTime start, LocalDateTime stop);
    @Query(
            value="select * from project where ( not (stop < :start or :stop < start) ) order by start",
            nativeQuery = true)
    Set<ProjectEntity> findInPeriod(LocalDateTime start, LocalDateTime stop);
    @Query(
            value="select * from project where ( not (stop < :start or :stop < start) ) " +
                    "and id in " +
                    "(select distinct project_id from roles where user_id in (select user_id from user_group where group_id = :group_id)) order by start",
            nativeQuery = true)
    Set<ProjectEntity> findInPeriodByGroup(LocalDateTime start, LocalDateTime stop, Long group_id);
}
