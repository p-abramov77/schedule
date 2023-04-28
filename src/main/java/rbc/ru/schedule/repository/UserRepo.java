package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import rbc.ru.schedule.entity.UserEntity;

import java.util.Set;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    Set<UserEntity> findByUsernameStartingWith(String name);

    @Query(value = "select username from users", nativeQuery = true)
    Set<String> findNames();
    Set<UserEntity> findByIdNotIn(Set<Long> userIds);
    Set<UserEntity> findAllByUsernameStartingWithOrderByUsername(String name);
    @Modifying
    @Transactional
    @Query(value = "delete from users where username = :name", nativeQuery = true)
    void deleteByUsername(String name);
}