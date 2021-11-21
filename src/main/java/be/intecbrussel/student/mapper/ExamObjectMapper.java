package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.entity.ExamEntity;
import org.springframework.stereotype.Component;

@Component
public class ExamObjectMapper implements IExamObjectMapper {

    @Override
    public ExamEntity toEntity(ExamDto dto) {

        final var entity = new ExamEntity();

        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getCode() != null) entity.setCode(dto.getCode());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (dto.getSession() != null) entity.setSession(dto.getSession());
        if (dto.getStartTime() != null) entity.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
        if (dto.getStudent() != null && dto.getStudent().getId() != null) entity.setStudentId(dto.getStudent().getId());
        if (dto.getTask() != null && dto.getTask().getId() != null) entity.setTaskId(dto.getTask().getId());
        if (dto.getScore() != null) entity.setScore(dto.getScore());
        if (dto.getSelected() != null) {
            entity.setSelected(dto.getSelected());
        } else {
            entity.setSelected(false);
        }
        if (dto.getSubmitted() != null) {
            entity.setSubmitted(dto.getSubmitted());
        } else {
            entity.setSubmitted(false);
        }


        return entity;
    }

    @Override
    public ExamDto toDTO(ExamEntity entity) {

        final var dto = new ExamDto();

        if (entity.getId() != null) dto.setId(entity.getId());
        if (entity.getCode() != null) dto.setCode(entity.getCode());
        if (entity.getActive() != null) dto.setActive(entity.getActive());
        if (entity.getActive() != null) dto.setActive(entity.getActive());
        if (entity.getSession() != null) dto.setSession(entity.getSession());
        if (entity.getStartTime() != null) dto.setStartTime(entity.getStartTime());
        if (entity.getEndTime() != null) dto.setEndTime(entity.getEndTime());
        if (entity.getStudentId() != null) dto.setStudentId(entity.getStudentId());
        if (entity.getTaskId() != null) dto.setTaskId(entity.getTaskId());
        if (entity.getScore() != null) dto.setScore(entity.getScore());
        if (entity.getSelected() != null) {
            dto.setSelected(entity.getSelected());
        } else {
            dto.setSelected(false);
        }
        if (entity.getSubmitted() != null) {
            dto.setSubmitted(entity.getSubmitted());
        } else {
            dto.setSubmitted(false);
        }

        return dto;
    }
}
