package com.rodrigo.tickets.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.entity.UserRole;
import com.rodrigo.tickets.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class InitialUserConfig {

    private static final String ADMIN_NAME = "Administrator";
    private static final String ADMIN_EMAIL = "admin@tickets.local";
    private static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialUserConfig(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void initializeAdmin(){
        createUserIfNotExists(userRepository, passwordEncoder, ADMIN_NAME, ADMIN_EMAIL, ADMIN_PASSWORD, UserRole.ADMIN);
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
        User user = new User(name, email, passwordEncoder.encode(rawPassword), role);
        userRepository.save(user);
        System.out.println("User created: " + user.getEmail());
    }
}
