package ru.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();

                admin.setName("Nikita");
                admin.setEmail("admin@mail.ru");
                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("admin")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("ADMIN CREATED");
            }
        };
    }
}