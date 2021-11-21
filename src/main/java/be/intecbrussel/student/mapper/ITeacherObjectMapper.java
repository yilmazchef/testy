package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.data.entity.UserEntity;

public interface ITeacherObjectMapper {

    TeacherEntity toEntity(TeacherDto dto);

    UserEntity toUserEntity(TeacherDto dto);

    TeacherDto toDTO(TeacherEntity entity);
}
