package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;

import java.util.Arrays;
import java.util.HashSet;

public class UserObjectMapper implements IUserObjectMapper {

    @Override
    public UserEntity toEntity(UserDto dto) {
        final var entity = new UserEntity();

        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (dto.getAuthenticated() != null) entity.setAuthenticated(dto.getAuthenticated());
        if (dto.getRoles() != null) entity.setRoles(String.join("", dto.getRoles()));
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
        if (dto.getActivation() != null) entity.setActivation(dto.getActivation());

        return entity;
    }

    @Override
    public UserDto toDTO(UserEntity entity) {
        final var dto = new UserDto();

        if (entity.getId() != null) dto.setId(entity.getId());
        if (entity.getActive() != null) dto.setActive(entity.getActive());
        if (entity.getAuthenticated() != null) dto.setAuthenticated(entity.getAuthenticated());
        if (entity.getRoles() != null) dto.setRoles(new HashSet<>(Arrays.asList(entity.getRoles().split(","))));
        if (entity.getUsername() != null) dto.setUsername(entity.getUsername());
        if (entity.getPassword() != null) dto.setPassword(entity.getPassword());
        if (entity.getActivation() != null) dto.setActivation(entity.getActivation());

        return dto;
    }
}
