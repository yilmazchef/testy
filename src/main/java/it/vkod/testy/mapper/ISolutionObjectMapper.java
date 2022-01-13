package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.SolutionDto;
import it.vkod.testy.data.entity.SolutionEntity;

public interface ISolutionObjectMapper {

    SolutionEntity toEntity(SolutionDto dto);

    SolutionDto toDTO(SolutionEntity entity);
}
