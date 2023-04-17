package rbc.ru.schedule;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import rbc.ru.schedule.entity.UserEntity;
import rbc.ru.schedule.repository.UserRepo;

@SpringBootApplication
public class ScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleApplication.class, args);
	}

//	@Bean
//	public ApplicationRunner dataLoader(
//			UserRepo repo, PasswordEncoder encoder) {
//		return args -> {
//			UserEntity userEntity = new UserEntity();
//			userEntity.setUsername("abrams");
//			userEntity.setPassword(encoder.encode("password"));
//			userEntity.setEnabled(true);
//			repo.save(userEntity);
//		};
//	}

}
