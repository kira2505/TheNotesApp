package com.notesapp.mapper;

import com.notesapp.dto.UserCreateDto;
import com.notesapp.dto.UserResponseDto;
import com.notesapp.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    @Mapping(source = "password", target = "passwordHash")
    AppUser toEntity(UserCreateDto dto);

    UserResponseDto toDto(AppUser user);
}
