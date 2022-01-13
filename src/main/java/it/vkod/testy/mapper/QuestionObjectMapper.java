package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.QuestionDto;
import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.entity.QuestionEntity;
import it.vkod.testy.data.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class QuestionObjectMapper implements IQuestionObjectMapper {

    @Override
    public QuestionEntity toEntity(QuestionDto dto) {

        final var entity = new QuestionEntity();
        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (dto.getHeader() != null) entity.setHeader(dto.getHeader());
        if (dto.getContent() != null) entity.setContent(dto.toString());
        if (dto.getOrderNo() != null) entity.setOrderNo(dto.getOrderNo());
        if (dto.getWeight() != null) entity.setWeight(dto.getWeight());
        if (dto.getTeacher() != null && dto.getTeacher().getId() != null) entity.setTeacherId(dto.getTeacher().getId());

        return entity;
    }

    @Override
    public QuestionDto toDTO(QuestionEntity entity) {
        final var dto = new QuestionDto();
        if (entity.getId() != null) dto.setId(entity.getId());
        if (entity.getActive() != null) dto.setActive(entity.getActive());
        if (entity.getHeader() != null) dto.setHeader(entity.getHeader());
        if (entity.getContent() != null) dto.setContent(Arrays.asList(entity.getContent().split("\n\n").clone()));
        if (entity.getOrderNo() != null) dto.setOrderNo(entity.getOrderNo());
        if (entity.getWeight() != null) dto.setWeight(entity.getWeight());
        if (entity.getTeacherId() != null) dto.setTeacherId(entity.getTeacherId());

        return dto;
    }

    @Override
    public QuestionDto toDTO(QuestionEntity questionEntity, Set<TaskEntity> taskEntities) {
        final var questionDto = new QuestionDto();
        if (questionEntity.getId() != null) questionDto.setId(questionEntity.getId());
        if (questionEntity.getActive() != null) questionDto.setActive(questionEntity.getActive());
        if (questionEntity.getHeader() != null) questionDto.setHeader(questionEntity.getHeader());
        if (questionEntity.getContent() != null)
            questionDto.setContent(Arrays.asList(questionEntity.getContent().split("\n\n").clone()));
        if (questionEntity.getOrderNo() != null) questionDto.setOrderNo(questionEntity.getOrderNo());
        if (questionEntity.getWeight() != null) questionDto.setWeight(questionEntity.getWeight());
        if (questionEntity.getTeacherId() != null) questionDto.setTeacherId(questionEntity.getTeacherId());

        return questionDto.withTasks(taskEntities
                .stream()
                .map(taskEntity -> {
                    final var taskDto = new TaskDto();
                    if (taskEntity != null) {
                        if (taskEntity.getId() != null) taskDto.setId(taskEntity.getId());
                        if (taskEntity.getActive() != null) taskDto.setActive(taskEntity.getActive());
                        if (taskEntity.getType() != null) taskDto.setType(taskEntity.getType());
                        if (taskEntity.getOrderNo() != null) taskDto.setOrderNo(taskEntity.getOrderNo());
                        if (taskEntity.getValue() != null) taskDto.setValue(taskEntity.getValue());
                        if (questionEntity.getId() != null) taskDto.setQuestionId(questionEntity.getId());
                        if (taskEntity.getWeight() != null) taskDto.setWeight(taskEntity.getWeight());
                    }
                    return taskDto;
                })
                .toArray(TaskDto[]::new));
    }
}
