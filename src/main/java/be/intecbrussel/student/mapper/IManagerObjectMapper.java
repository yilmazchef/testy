package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.ManagerDto;
import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.data.entity.UserEntity;

public interface IManagerObjectMapper {

    ManagerEntity toEntity(ManagerDto dto);

    UserEntity toUserEntity(ManagerDto dto);

    ManagerDto toDTO(ManagerEntity entity);
}
