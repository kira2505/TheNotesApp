package com.notesapp.service;

import com.notesapp.enums.Role;
import com.notesapp.exception.UserNotFoundException;
import com.notesapp.model.AppUser;
import com.notesapp.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        AppUser user = new AppUser();
        user.setPasswordHash("12345");

        when(passwordEncoder.encode("12345")).thenReturn("hashed-pass");
        when(userRepository.save(any(AppUser.class))).thenAnswer(i -> i.getArgument(0));

        AppUser saved = userService.register(user, "12345");

        assertEquals("hashed-pass", saved.getPasswordHash());
        assertEquals(Role.ROLE_USER, saved.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void register_nullPassword_throwsException() {
        AppUser user = new AppUser();
        assertThrows(IllegalArgumentException.class, () -> userService.register(user, null));
    }

    @Test
    void register_emptyPassword_throwsException() {
        AppUser user = new AppUser();
        assertThrows(IllegalArgumentException.class, () -> userService.register(user, " "));
    }

    @Test
    void getByUsername_success() {
        AppUser user = new AppUser();
        user.setUsername("kira");

        when(userRepository.findByUsername("kira")).thenReturn(Optional.of(user));

        AppUser result = userService.getByUsername("kira");

        assertEquals("kira", result.getUsername());
    }

    @Test
    void getByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getByUsername("ghost"));
    }

    @Test
    void getAppUser_success() {
        AppUser mockUser = new AppUser();
        mockUser.setUsername("kira");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        AppUser result = userService.getAppUser();

        assertEquals("kira", result.getUsername());
    }

}