package com.rodrigo.tickets.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.entity.UserRole;
import com.rodrigo.tickets.repository.UserRepository;

@Configuration
public class InitialUserConfig {
    
    private final UserRepository userRepository;

    InitialUserConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner createInitialAdmin(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
        return args -> {
            String adminEmail = "admin@tickets.local";
            if(userRepository.findByEmail(adminEmail).isEmpty()){
                User admin = new User("Administrator", adminEmail, passwordEncoder.encode("admin123"), UserRole.ADMIN);
                this.userRepository.save(admin);
                System.out.println("Admin user created");
            }

            String name = "Rodrigo";
            createUserIfNotExists(userRepository, passwordEncoder,
                 name, name + "@tickets.local", passwordEncoder.encode(name+"123"), UserRole.ADMIN);

            name = "Gabriel";
            createUserIfNotExists(userRepository, passwordEncoder,
                 name, name + "@tickets.local", passwordEncoder.encode(name+"123"), UserRole.AGENT);
            
            name = "Mariana";
            createUserIfNotExists(userRepository, passwordEncoder,
                 name, name + "@tickets.local", passwordEncoder.encode(name+"123"), UserRole.ADMIN);

            name = "Dummy";
            createUserIfNotExists(userRepository, passwordEncoder,
                 name, name + "@tickets.local", passwordEncoder.encode(name+"123"), UserRole.AGENT);
        };
    }

    private void createUserIfNotExists(UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        String name,
        String email,
        String rawPassword,
        UserRole role
    ){
        if(userRepository.findByEmail(email).isPresent()) {
            System.out.println(email + " already exists.");
            return;
        }
        User user = new User(name, email, rawPassword, role);
        userRepository.save(user);
        System.out.println("User created: " + user.getEmail());
    }
}
