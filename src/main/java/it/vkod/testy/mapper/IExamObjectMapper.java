package it.vkod.testy.mapper;

import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.entity.ExamEntity;

public interface IExamObjectMapper {

    ExamEntity toEntity(ExamDto dto);

    ExamDto toDTO(ExamEntity entity);
}
