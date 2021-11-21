package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.data.entity.StudentEntity;
import be.intecbrussel.student.data.entity.UserEntity;

public interface IStudentObjectMapper {

    StudentEntity toEntity(StudentDto dto);

    UserEntity toUserEntity(StudentDto dto);

    StudentDto toDTO(StudentEntity entity);
}
