package rbc.ru.schedule.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rbc.ru.schedule.entity.ToDoEntity;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.UserRepo;
import rbc.ru.schedule.service.UserServiceImpl;

import javax.annotation.PostConstruct;

@Component
public class UserValidator {
    @Autowired
    private UserServiceImpl userService;

    @Value("${superuser.login}")
    public String superLogin;
    @Value("${superuser.password}")
    public String superPassword;
    public static String login;

    @Bean
	public ApplicationRunner dataLoader(
            UserRepo repo, PasswordEncoder encoder) {
        return args -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(superLogin);
            userEntity.setPassword(encoder.encode(superPassword));
            userEntity.setEnabled(true);
            userEntity.setMaker(true);
            userEntity.setEmail("aa");
            repo.deleteByUsername(superLogin);
            repo.save(userEntity);
        };
    }
    @PostConstruct
    public void setLogin() {
        login = superLogin;
    }
    public boolean isAdmin(String userName) {

        return login.equals(userName);
    }

    public  boolean isTodoMember(String username, ToDoEntity toDoEntity) {
        //TODO написать isTodoMember
        return true;
    }
    public boolean isTodoProducer(String username, ToDoEntity toDoEntity) {
        //TODO написать isTodoProducer
        return true;
    }
    public boolean isTodoExecutor(String username, ToDoEntity toDoEntity) {
        //TODO написать isTodoExecutor
        return true;
    }
}
