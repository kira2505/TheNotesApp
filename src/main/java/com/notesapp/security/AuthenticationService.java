package com.notesapp.security;

import com.notesapp.dto.LoginRequestDto;

public interface AuthenticationService {

    String login(LoginRequestDto loginRequestDto);
}
