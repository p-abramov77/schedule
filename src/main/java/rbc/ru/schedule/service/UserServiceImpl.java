package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.RoleEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.UserRepo;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public boolean save(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        try{
            userRepo.save(userEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<UserEntity> listUsers(String name) {
        return userRepo.findByUsernameStartingWith(name);
    }

    @Override
    public Set<String> listNames() {
        return userRepo.findNames();
    }

    @Override
    public Set<UserEntity> getAllUsers() {
        return userRepo.findByUsernameStartingWith("");
    }

    @Override
    public Set<UserEntity> available(Set<RoleEntity> roleEntities) {
        Set<Long> longs = new HashSet<>();
        for (RoleEntity role : roleEntities ) {
            longs.add(role.getId());
        }
        return userRepo.findByIdNotIn(longs);
    }

    @Override
    public void deleteByUsername(String name) {
        userRepo.deleteByUsername(name);
    }
}
