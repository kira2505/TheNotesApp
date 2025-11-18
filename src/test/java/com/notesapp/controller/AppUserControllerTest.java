package com.notesapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notesapp.dto.LoginRequestDto;
import com.notesapp.dto.UserCreateDto;
import com.notesapp.dto.UserResponseDto;
import com.notesapp.mapper.AppUserMapper;
import com.notesapp.model.AppUser;
import com.notesapp.security.AuthenticationService;
import com.notesapp.security.JwtService;
import com.notesapp.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AppUserMapper appUserMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testRegisterUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("testuser", "password");
        AppUser user = new AppUser();
        user.setId("user1");
        user.setUsername("testuser");

        UserResponseDto responseDto = new UserResponseDto("user1", "testuser");

        Mockito.when(appUserMapper.toEntity(createDto)).thenReturn(user);
        Mockito.when(appUserService.register(user, createDto.password())).thenReturn(user);
        Mockito.when(appUserMapper.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated()) // <- здесь ожидаем 201, а не 200
                .andExpect(jsonPath("$.id").value("user1"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testLoginUser() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("testuser", "password");
        String token = "mocked-jwt-token";

        Mockito.when(authenticationService.login(loginRequest)).thenReturn(token);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }
}