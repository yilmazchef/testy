package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.SolutionDto;
import be.intecbrussel.student.data.entity.SolutionEntity;

public interface ISolutionObjectMapper {

    SolutionEntity toEntity(SolutionDto dto);

    SolutionDto toDTO(SolutionEntity entity);
}
