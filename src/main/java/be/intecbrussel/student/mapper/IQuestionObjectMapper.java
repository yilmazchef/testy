package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.entity.QuestionEntity;
import be.intecbrussel.student.data.entity.TaskEntity;

import java.util.Set;

public interface IQuestionObjectMapper {

    QuestionEntity toEntity(QuestionDto dto);

    QuestionDto toDTO(QuestionEntity entity);

    QuestionDto toDTO(QuestionEntity questionEntity, Set<TaskEntity> taskEntities);
}
