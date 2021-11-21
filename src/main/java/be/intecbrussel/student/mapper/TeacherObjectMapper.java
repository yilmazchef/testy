package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class TeacherObjectMapper implements ITeacherObjectMapper {

    @Override
    public TeacherEntity toEntity(TeacherDto dto) {

        final var entity = new TeacherEntity();
        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (dto.getAuthenticated() != null) entity.setAuthenticated(dto.getAuthenticated());
        if (dto.getRoles() != null) entity.setRoles(String.join("", dto.getRoles()));
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
        if (dto.getActivation() != null) entity.setActivation(dto.getActivation());
        if (dto.getAnonymous() != null) entity.setAnonymous(dto.getAnonymous());
        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
        if (dto.getDepartment() != null) entity.setDepartment(dto.getDepartment());

        return entity;
    }

    @Override
    public UserEntity toUserEntity(TeacherDto dto) {

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
    public TeacherDto toDTO(TeacherEntity entity) {

        final var dto = new TeacherDto();

        if (entity.getId() != null) dto.setId(entity.getId());
        if (entity.getActive() != null) dto.setActive(entity.getActive());
        if (entity.getAuthenticated() != null) dto.setAuthenticated(entity.getAuthenticated());
        if (entity.getRoles() != null) dto.setRoles(new HashSet<>(Arrays.asList(entity.getRoles().split(""))));
        if (entity.getUsername() != null) dto.setUsername(entity.getUsername());
        if (entity.getPassword() != null) dto.setPassword(entity.getPassword());
        if (entity.getActivation() != null) dto.setActivation(entity.getActivation());
        if (entity.getAnonymous() != null) dto.setAnonymous(entity.getAnonymous());
        if (entity.getFirstName() != null) dto.setFirstName(entity.getFirstName());
        if (entity.getLastName() != null) dto.setLastName(entity.getLastName());
        if (entity.getDepartment() != null) dto.setDepartment(entity.getDepartment());

        return dto;
    }
}
