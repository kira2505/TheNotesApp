package com.notesapp.controller;

import com.notesapp.dto.LoginRequestDto;
import com.notesapp.dto.UserCreateDto;
import com.notesapp.dto.UserResponseDto;
import com.notesapp.mapper.AppUserMapper;
import com.notesapp.model.AppUser;
import com.notesapp.security.AuthenticationService;
import com.notesapp.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AppUserController implements AppUserApi {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public UserResponseDto register(@Valid @RequestBody UserCreateDto userCreateDto) {
        AppUser user = appUserMapper.toEntity(userCreateDto);
        AppUser savedUser = appUserService.register(user, userCreateDto.password());
        return appUserMapper.toDto(savedUser);
    }

    @Override
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.login(loginRequestDto);
    }
}
