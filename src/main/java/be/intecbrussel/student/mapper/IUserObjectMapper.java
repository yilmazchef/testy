package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;

public interface IUserObjectMapper {

    UserEntity toEntity(UserDto dto);

    UserDto toDTO(UserEntity entity);
}
