package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.entity.ExamEntity;

public interface IExamObjectMapper {

    ExamEntity toEntity(ExamDto dto);

    ExamDto toDTO(ExamEntity entity);
}
