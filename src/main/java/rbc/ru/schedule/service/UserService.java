package rbc.ru.schedule.service;

import rbc.ru.schedule.entity.UserEntity;

import java.util.Set;

public interface UserService {
    UserEntity getById(Long id);
    UserEntity findUserByUsername(String username);
    public boolean save(UserEntity userEntity);
    public Set<UserEntity> listUsers(String name);
    public Set<String> listNames();
}
