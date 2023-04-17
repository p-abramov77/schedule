package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.UserRepo;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

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
}
