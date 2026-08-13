package com.rodrigo.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Transactional
    public List<User> findAllEnabled(){
        return userRepository.findAll()
        .stream()
        .filter(User::isEnabled)
        .toList();
    }
}
