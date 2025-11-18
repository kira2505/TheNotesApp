package com.notesapp.security;

import com.notesapp.model.AppUser;

public interface JwtService {

    String generateToken(AppUser user);

    String extractUserName(String token);

    boolean isTokenValid(String token);
}
