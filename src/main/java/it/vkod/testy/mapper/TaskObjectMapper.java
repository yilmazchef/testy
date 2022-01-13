package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TaskObjectMapper implements ITaskObjectMapper {

    @Override
    public TaskEntity toEntity(TaskDto dto) {

        final var entity = new TaskEntity();
        if (dto != null) {
            if (dto.getId() != null) entity.setId(dto.getId());
            if (dto.getActive() != null) entity.setActive(dto.getActive());
            entity.setType(dto.getType() == null ? TaskDto.Type.UNDEFINED.name() : Objects.requireNonNull(dto.getType().name()));
            if (dto.getOrderNo() != null) entity.setOrderNo(dto.getOrderNo());
            if (dto.getValue() != null) entity.setValue(dto.getValue());
            if (dto.getQuestion() != null && dto.getQuestion().getId() != null)
                entity.setQuestionId(dto.getQuestion().getId());
            if (dto.getWeight() != null) entity.setWeight(dto.getWeight());
        }

        return entity;
    }

    @Override
    public TaskDto toDTO(TaskEntity entity) {

        final var dto = new TaskDto();

        if (entity != null) {
            if (entity.getId() != null) dto.setId(entity.getId());
            if (entity.getActive() != null) dto.setActive(entity.getActive());
            if (entity.getType() != null) dto.setType(entity.getType());
            if (entity.getOrderNo() != null) dto.setOrderNo(entity.getOrderNo());
            if (entity.getValue() != null) dto.setValue(entity.getValue());
            if (entity.getQuestionId() != null) dto.setQuestionId(entity.getQuestionId());
            if (entity.getWeight() != null) dto.setWeight(entity.getWeight());
        }

        return dto;
    }

}
