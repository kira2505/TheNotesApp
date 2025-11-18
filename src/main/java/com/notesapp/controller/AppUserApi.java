package com.notesapp.controller;

import com.notesapp.dto.LoginRequestDto;
import com.notesapp.dto.UserCreateDto;
import com.notesapp.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Users API", description = "Here you can register new users or log into the system.")
public interface AppUserApi {

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseDto register(@RequestBody UserCreateDto userCreateDto);

    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token")
    @PostMapping("/login")
    String login(@RequestBody LoginRequestDto loginRequestDto);
}

