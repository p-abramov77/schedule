package rbc.ru.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rbc.ru.schedule.entity.UserEntity;

import java.util.Set;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    Set<UserEntity> findByUsernameStartingWith(String name);

    @Query(value = "select username from users", nativeQuery = true)
    Set<String> findNames();
}