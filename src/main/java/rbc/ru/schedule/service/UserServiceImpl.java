package rbc.ru.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.UserRepo;
import rbc.ru.schedule.validator.UserValidator;

import java.util.Set;
import java.util.stream.Collectors;

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
        return userRepo.findByUsernameOrderByUsername(username);
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

        return userRepo.findByUsernameStartingWithOrderByUsername(name)
                        .stream()
                        .filter(u -> !u.getUsername().equals(UserValidator.login))
                        .collect(Collectors.toSet());
    }

    @Override
    public Set<String> listNames() {
        return userRepo.findNames();
    }

    @Override
    public Set<UserEntity> getAllUsers() {

        return userRepo.findByUsernameStartingWithOrderByUsername("")
                .stream()
                .filter(u -> !u.getUsername().equals(UserValidator.login))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserEntity> available(Long project_id) {
        return userRepo.available(project_id)
                .stream()
                .filter(u -> !u.getUsername().equals(UserValidator.login))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteByUsername(String name) {
        userRepo.deleteByUsername(name);
    }

    @Override
    public Set<UserEntity> getAllExecutorsOfProject(Long project_id) {
        return userRepo.getAllExecutorsOfProject(project_id);
    }
}
