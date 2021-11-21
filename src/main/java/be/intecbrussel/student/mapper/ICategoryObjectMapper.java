package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.CategoryDto;
import be.intecbrussel.student.data.entity.CategoryEntity;

public interface ICategoryObjectMapper {

    CategoryEntity toEntity(CategoryDto dto);

    CategoryDto toDTO(CategoryEntity entity);
}
