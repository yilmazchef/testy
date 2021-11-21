package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.CategoryDto;
import be.intecbrussel.student.data.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryObjectMapper implements ICategoryObjectMapper {

    @Override
    public CategoryEntity toEntity(CategoryDto dto) {

        final var entity = new CategoryEntity();

        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (dto.getValue() != null) entity.setValue(dto.getValue());
        if (dto.getParent() != null && dto.getParent().getId() != null) entity.setParentId(dto.getParent().getId());

        return entity;
    }

    @Override
    public CategoryDto toDTO(CategoryEntity entity) {

        final var dto = new CategoryDto();

        if (entity.getId() != null) dto.setId(entity.getId());
        if (entity.getActive() != null) dto.setActive(dto.getActive());
        if (entity.getValue() != null) dto.setValue(entity.getValue());
        if (entity.getParentId() != null) dto.setParentId(entity.getParentId());

        return dto;
    }
}
