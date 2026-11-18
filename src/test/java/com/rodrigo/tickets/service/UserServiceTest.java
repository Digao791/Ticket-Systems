package com.rodrigo.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.repository.UserRepository;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        service = new UserService(userRepository);
    }

    @Test
    void findByEmailReturnsMatchingUser() {
        User user = mock(User.class);
        when(userRepository.findByEmail("ana@tickets.local")).thenReturn(Optional.of(user));

        assertEquals(user, service.findByEmail("ana@tickets.local"));
    }

    @Test
    void findByEmailThrowsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@tickets.local")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
            () -> service.findByEmail("missing@tickets.local"));
    }

    @Test
    void findAllEnabledReturnsOnlyEnabledUsers() {
        User enabled = mock(User.class);
        User disabled = mock(User.class);
        when(enabled.isEnabled()).thenReturn(true);
        when(disabled.isEnabled()).thenReturn(false);
        when(userRepository.findAll()).thenReturn(List.of(enabled, disabled));

        assertEquals(List.of(enabled), service.findAllEnabled());
    }
}
