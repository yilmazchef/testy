package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.QuestionDto;
import it.vkod.testy.data.entity.QuestionEntity;
import it.vkod.testy.data.entity.TaskEntity;

import java.util.Set;

public interface IQuestionObjectMapper {

    QuestionEntity toEntity(QuestionDto dto);

    QuestionDto toDTO(QuestionEntity entity);

    QuestionDto toDTO(QuestionEntity questionEntity, Set<TaskEntity> taskEntities);
}
