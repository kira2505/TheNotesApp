package com.notesapp.security;

import com.notesapp.dto.LoginRequestDto;
import com.notesapp.model.AppUser;
import com.notesapp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private JwtService jwtService;

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.login(), loginRequestDto.password()));

        AppUser user = appUserService.getByUsername(loginRequestDto.login());
        return jwtService.generateToken(user);
    }
}
