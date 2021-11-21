package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.SolutionDto;
import be.intecbrussel.student.data.entity.SolutionEntity;

public class SolutionObjectMapper implements ISolutionObjectMapper {

    @Override
    public SolutionEntity toEntity(SolutionDto dto) {
        SolutionEntity entity = new SolutionEntity();

        if (dto != null) {
            if (dto.getId() != null) entity.setId(dto.getId());
            if(dto.getTeacher() != null && dto.getTeacher().getId() != null) entity.setTeacherId(dto.getTeacher().getId());
            if(dto.getExam() != null && dto.getExam().getId() != null) entity.setExamId(dto.getExam().getId());
            if(dto.getContent() != null) entity.setContent(entity.getContent());
            if(dto.getLikes() != null && dto.getLikes() >= 0) entity.setLikes(dto.getLikes());
            if(dto.getDisLikes() != null && dto.getDisLikes() >= 0) entity.setDisLikes(dto.getDisLikes());
            if(dto.getActive() != null) entity.setActive(dto.getActive());
        }

        return entity;
    }

    @Override
    public SolutionDto toDTO(SolutionEntity entity) {
        SolutionDto dto = new SolutionDto();

        if (entity != null) {
            if (entity.getId() != null) dto.setId(dto.getId());
            if(entity.getTeacherId() != null && dto.getTeacher().getId() != null) dto.setTeacherId(dto.getTeacher().getId());
            if(entity.getExamId() != null && dto.getExam().getId() != null) dto.setExamId(dto.getExam().getId());
            if(entity.getContent() != null) dto.setContent(entity.getContent());
            if(entity.getLikes() != null && dto.getLikes() >= 0) dto.setLikes(dto.getLikes());
            if(entity.getDisLikes() != null && dto.getDisLikes() >= 0) dto.setDisLikes(dto.getDisLikes());
            if(entity.getActive() != null) dto.setActive(dto.getActive());
        }

        return dto;
    }
}
