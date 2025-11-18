package com.notesapp.service;

import com.notesapp.model.AppUser;

public interface AppUserService {

    AppUser register(AppUser appUser, String rawPassword);

    AppUser getByUsername(String username);

    AppUser getAppUser();
}
