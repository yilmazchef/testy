package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.entity.TaskEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ITaskObjectMapper {

    TaskEntity toEntity(TaskDto dto);

    TaskDto toDTO(TaskEntity entity);

    default List<TaskEntity> toEntities(List<TaskDto> dtos) {
        return dtos
                .stream()
                .map(this::toEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    default List<TaskDto> toDTOs(List<TaskEntity> entities) {
        return entities
                .stream()
                .map(this::toDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }
}
