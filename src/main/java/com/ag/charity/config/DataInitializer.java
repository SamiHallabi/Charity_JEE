package com.ag.charity.config;

import com.ag.charity.entities.enums.Role;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner createSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@charity.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin1234!"));
                admin.setFirstName("Super");
                admin.setLastName("Admin");
                admin.setRole(Role.SUPER_ADMIN);
                userRepository.save(admin);
                System.out.println(">>> Super admin created: " + adminEmail + " / Admin1234!");
            }
        };
    }
}
