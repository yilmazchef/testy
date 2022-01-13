package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.CategoryDto;
import it.vkod.testy.data.entity.CategoryEntity;

public interface ICategoryObjectMapper {

    CategoryEntity toEntity(CategoryDto dto);

    CategoryDto toDTO(CategoryEntity entity);
}
