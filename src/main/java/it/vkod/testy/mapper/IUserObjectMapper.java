package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.UserDto;
import it.vkod.testy.data.entity.UserEntity;

public interface IUserObjectMapper {

    UserEntity toEntity(UserDto dto);

    UserDto toDTO(UserEntity entity);
}
